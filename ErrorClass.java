package com.rogger.test;

public class ErrorClass {
	public static String getErrorStr(String txt) {
		String msg ="";
		if(!txt.startsWith(".")&&txt.isEmpty()){
		  msg = "Dados invalido!";	
		}
		if(txt.length() <4){
			msg = "Nome invaido!";
		}
		return msg;
	}
	public static String getErrorInt(int n){
		String msg ="";
		if(n==0){
			msg = "Valor precissa ser preenchido!";
		}
	return msg;	
	}
	/*
	private boolean Invalidate() {
		return (!editaltura.getText().toString().startsWith("0") &&
		!editaltura.getText().toString().startsWith("0")
		&& !editaltura.getText().toString().isEmpty()
		&& !editpeso.getText().toString().isEmpty());
		*/
}
