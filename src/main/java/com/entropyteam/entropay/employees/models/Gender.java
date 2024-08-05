package com.entropyteam.entropay.employees.models;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Gender{MALE, FEMALE, NO_BINARY}

