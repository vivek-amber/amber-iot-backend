package com.device.service;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.RuntimeCryptoException;
import org.springframework.stereotype.Service;

import com.device.dto.DeviceChallangeResponse;
import com.device.dto.DeviceChallangerequest;
import com.device.dto.DeviceClaim;
import com.device.dto.DeviceSignatureVerifyRequest;
import com.device.dto.DeviceSignatureVerifyResponse;
import com.device.helper.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProvisionService {

    private final ConcurrentHashMap<String, byte[]> pendingChallenges = new ConcurrentHashMap<>();

    private static final String PUBLIC_KEY_PEM = """
                        -----BEGIN PUBLIC KEY-----
            MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEZmWIFBnBwOxPWZumN4piPtVS0BQq
            YiFjl3eZ++eaT1SVFgqVyRhN1MbVNLD0m1Ynzc944swQKJIWDSuN7VMQuA==
            -----END PUBLIC KEY-----
                        """;

    public ApiResponse<DeviceChallangeResponse> getChallangeNonce(DeviceChallangerequest request) {

        byte[] nonce = new byte[32];
        new SecureRandom().nextBytes(nonce);
        pendingChallenges.put(request.deviceId(), nonce);

        String nonceString = Base64.getEncoder().encodeToString(nonce);

        return ApiResponse.success("Nonce fetched.", new DeviceChallangeResponse(nonceString));
    }

    public ApiResponse<DeviceSignatureVerifyResponse> verifySignature(DeviceSignatureVerifyRequest request) {

        byte[] signature = Base64.getDecoder().decode(request.signature());
        byte[] expectedNonce = pendingChallenges.get(request.deviceId());
        byte[] receivedNonce = Base64.getDecoder().decode(request.nonce());

        if (!Arrays.equals(expectedNonce, receivedNonce)) {
            return ApiResponse.error("Invalid nonce");
        }

        if (!Arrays.equals(expectedNonce, receivedNonce)) {
            return ApiResponse.error("Invalid nonce");
        }

        try {

            byte[] derSig = rawToDer(signature);

            // signature verification
            PublicKey publicKey = loadPublicKey(PUBLIC_KEY_PEM);

            Signature verifier = Signature.getInstance("SHA256withECDSA");

            verifier.initVerify(publicKey);
            verifier.update(receivedNonce);

            boolean valid = verifier.verify(derSig);
            if (!valid) {
                return ApiResponse.error("Signature verification failed");
            }

            // claim generation

            DeviceClaim claim = new DeviceClaim(request.deviceId(), System.currentTimeMillis());

            ObjectMapper mapper = new ObjectMapper();
            String claimPayload = mapper.writeValueAsString(claim);

            String claimSignature = signClaim(claimPayload);
            pendingChallenges.remove(request.deviceId());

            return ApiResponse.success("Claim fetched successfully.",
                    new DeviceSignatureVerifyResponse(claimPayload, claimSignature));

        } catch (Exception e) {

            log.error("Verification failed", e);
            throw new RuntimeCryptoException(e.getMessage());
        }
    }

    private String signClaim(String claim) throws Exception {

        PrivateKey privateKey = loadServerPrivateKey();
        Signature sign = Signature.getInstance("SHA256withECDSA");

        sign.initSign(privateKey);
        sign.update(claim.getBytes());

        byte[] derSign = sign.sign();
        byte[] rawSignature = derToRaw(derSign);

        return Base64.getEncoder().encodeToString(rawSignature);
    }

    public PublicKey loadPublicKey(String pem) throws Exception {
        String key = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("EC");

        return factory.generatePublic(spec);
    }

    private PrivateKey loadServerPrivateKey() throws Exception {

        byte[] d = hexToBytes(
                "3D2D57577250D07422A0B5791CFAAE58B3FEC320E7773BE20878D420F372B175");

        AlgorithmParameters params = AlgorithmParameters.getInstance("EC");

        params.init(
                new ECGenParameterSpec("secp256r1"));

        ECParameterSpec ecSpec = params.getParameterSpec(
                ECParameterSpec.class);

        ECPrivateKeySpec keySpec = new ECPrivateKeySpec(
                new BigInteger(1, d),
                ecSpec);

        return KeyFactory
                .getInstance("EC")
                .generatePrivate(keySpec);
    }

    private byte[] hexToBytes(String s) {

        byte[] data = new byte[s.length() / 2];

        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) Integer.parseInt(
                    s.substring(i * 2, i * 2 + 2),
                    16);
        }

        return data;
    }

    private byte[] rawToDer(byte[] raw) throws Exception {

        byte[] r = Arrays.copyOfRange(raw, 0, 32);
        byte[] s = Arrays.copyOfRange(raw, 32, 64);

        BigInteger rInt = new BigInteger(1, r);
        BigInteger sInt = new BigInteger(1, s);

        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(rInt));
        v.add(new ASN1Integer(sInt));

        return new DERSequence(v).getEncoded();
    }

    private byte[] derToRaw(byte[] der) throws Exception {

        ASN1Sequence seq = ASN1Sequence.getInstance(der);

        BigInteger r = ((ASN1Integer) seq.getObjectAt(0))
                .getPositiveValue();

        BigInteger s = ((ASN1Integer) seq.getObjectAt(1))
                .getPositiveValue();

        byte[] raw = new byte[64];

        byte[] rBytes = r.toByteArray();
        byte[] sBytes = s.toByteArray();

        System.arraycopy(
                rBytes,
                Math.max(0, rBytes.length - 32),
                raw,
                32 - Math.min(32, rBytes.length),
                Math.min(32, rBytes.length));

        System.arraycopy(
                sBytes,
                Math.max(0, sBytes.length - 32),
                raw,
                64 - Math.min(32, sBytes.length),
                Math.min(32, sBytes.length));

        return raw;
    }
}
