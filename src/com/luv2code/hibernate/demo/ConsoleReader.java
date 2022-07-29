package com.luv2code.hibernate.demo;

import java.util.Scanner;

public class ConsoleReader {

    private Scanner scanner = new Scanner(System.in);

    public String readProperty(String info) {
        String property = "";
        while(property.equals("")){
            System.out.println(info);
            property = (scanner.nextLine()).trim();
        }
        return property;
    }

    public int readDecision(){
        return Integer.parseInt(scanner.nextLine());
    }

    public String fetchInput() {
        return (scanner.nextLine()).trim();
    }


}
