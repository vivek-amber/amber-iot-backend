package com.automation.dto;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleTrigger {

    @NotNull(message = "Schedule hour is required")
    @Min(value = 0, message = "Schedule hour must be between 0 and 23")
    @Max(value = 23, message = "Schedule hour must be between 0 and 23")
    private Integer hour;

    @NotNull(message = "Schedule minute is required")
    @Min(value = 0, message = "Schedule minute must be between 0 and 59")
    @Max(value = 59, message = "Schedule minute must be between 0 and 59")
    private Integer minute;

    @NotEmpty(message = "At least one repeat day is required")
    @Size(max = 7, message = "Repeat days cannot contain more than 7 values")
    private List<@NotBlank(message = "Repeat day cannot be blank") @Pattern(regexp = "MON|TUE|WED|THU|FRI|SAT|SUN", message = "Repeat day must be MON, TUE, WED, THU, FRI, SAT, or SUN") 
    String> repeatDays;

    @NotBlank(message = "Timezone is required")
    @Size(max = 100, message = "Timezone cannot exceed 100 characters")
    private String timezone;

}