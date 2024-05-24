package LLD.TextEditor;

import java.util.Stack;
public class StackBasedApproach {
}


class TextEditor {
    // why using stringBuilder -> they provide mutable objects!
    // (can be changed without creating new object with every concat/append operation)

    private StringBuilder text;
    private Stack<Operation> history;
    private Stack<Operation> redoHistory;

    private enum OperationType {
        APPEND,
        BACKSPACE
    }

    private static class Operation {
        OperationType type;
        String data;

        Operation(OperationType type, String data) {
            this.type = type;
            this.data = data;
        }
    }

    public TextEditor() {
        text = new StringBuilder();
        history = new Stack<>();
        redoHistory = new Stack<>();
    }

    public void append(String newData) {
        history.push(new Operation(OperationType.APPEND, newData));
        text.append(newData);
    }

    public void backspace() {
        if (text.length() > 0) {
            char lastChar = text.charAt(text.length() - 1);
            history.push(new Operation(OperationType.BACKSPACE, String.valueOf(lastChar)));
            text.deleteCharAt(text.length() - 1);
        }
    }

    public void undo() {
        if (!history.isEmpty()) {
            Operation lastOperation = history.pop();
            redoHistory.push(lastOperation);

            if (lastOperation.type == OperationType.APPEND) {
                int lengthToRemove = lastOperation.data.length();
                text.delete(text.length() - lengthToRemove, text.length());
            } else {
                text.append(lastOperation.data);
            }
        }
    }

    public void redo() {
        if (!redoHistory.isEmpty()) {
            Operation lastUndoneOperation = redoHistory.pop();
            history.push(lastUndoneOperation);

            if (lastUndoneOperation.type == OperationType.APPEND) {
                text.append(lastUndoneOperation.data);
            } else {
                text.deleteCharAt(text.length() - 1);
            }
        }
    }

    public String getText() {
        return text.toString();
    }

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();

        editor.append("Hello, ");
        System.out.println(editor.getText()); // Output: Hello,

        editor.append("world!");
        System.out.println(editor.getText()); // Output: Hello, world!

        editor.backspace();
        System.out.println(editor.getText()); // Output: Hello, world

        editor.undo();
        System.out.println(editor.getText()); // Output: Hello, world!

        editor.redo();
        System.out.println(editor.getText()); // Output: Hello, world
    }
}