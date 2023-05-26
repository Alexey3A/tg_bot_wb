package com.example.tg_bot_wb.exeption;

public class ProductAbsenceException extends RuntimeException{

  public String getMessage(){
      return "Товар отсутствует";
  }
}
