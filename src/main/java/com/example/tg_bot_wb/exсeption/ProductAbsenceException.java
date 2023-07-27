package com.example.tg_bot_wb.exсeption;

public class ProductAbsenceException extends RuntimeException{

  public String getMessage(){
      return "Товар отсутствует";
  }
}
