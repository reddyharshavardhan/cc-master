package com.example.currency_converter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRequest {
	private String from; // Source currency (e.g., USD)
	private String to; // Target currency (e.g., EUR)
	private Double amount; // Amount to be converted
}
