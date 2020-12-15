package edu.pwr.checkers.client.view;

import edu.pwr.checkers.client.Mediator;
import org.junit.Test;

import java.util.Scanner;

public class ClientWindowTest {

    // this isn't a proper test, I use it to see if everything displays correctly
    @Test
    public void testConstructor() {
        new Mediator();
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        scanner.close();
    }
}
