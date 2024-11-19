import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create a new frame
        JFrame frame = new JFrame("Swing Example");
        
        // Set frame properties
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create a button and add it to the frame
        JButton button = new JButton("Click Me");
        frame.add(button);
        
        // Make the frame visible
        frame.setVisible(true);
    }
}
