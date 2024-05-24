package LLD.TextEditor;



// Here we will also support operations for moving the cursor to Left and Right!

import java.util.Stack;

class CharNode{
    char ch;
    CharNode prev;
    CharNode next;

    public CharNode(char ch){
        this.ch=ch;
        prev=null;
        next=null;
    }
}

enum OperationType{
    DEL, INS, LEFT, RIGHT;
}

class Edit{
    OperationType operation;
    CharNode data;

    public Edit(OperationType operation, CharNode data){
        this.operation =  operation;
        this.data = data;
    }
}

class TextEditor2{
    private CharNode cursor;
    private CharNode end;
    private Stack<Edit> undoStack;
    private Stack<Edit> redoStack;

    public TextEditor2(){
        this.end = new CharNode('\0');
        this.cursor = end;
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void moveLeft(){
        if(cursor.prev==null) return;
        cursor = cursor.prev;
        undoStack.push(new Edit(OperationType.LEFT, null));
    }

    public void moveRight(){
        if(cursor==end) return;
        cursor = cursor.next;
        undoStack.push(new Edit(OperationType.RIGHT, null));
    }

    // we are always always inserting just before the cursor, cursor is always ahead!!!

    public void insertCharacter(char ch){
        CharNode node = new CharNode(ch);
        insert(node);
        undoStack.push(new Edit(OperationType.INS, node));
    }

    public void backSpace(){
        if(cursor.prev==null) return;
        CharNode del = cursor.prev;
        delete(del);
        undoStack.push(new Edit(OperationType.DEL, del));
    }

    public void delete(CharNode del){
        cursor.prev = del.prev;
        if(del.prev!=null){
            del.prev.next = cursor;
        }
    }

    public void insert(CharNode node){
        if(cursor.prev!=null){
            node.prev = cursor.prev;
            cursor.prev.next = node;
        }
        node.next = cursor;
        cursor.prev = node;
    }

    public void undo(){
        if(undoStack.isEmpty()) return;
        Edit edit = undoStack.pop();
        switch (edit.operation){
            case INS:
                // we need to delete this
                delete(cursor.prev);
                // put in redo stack if want to implement that as stack example
                break;
            case DEL:
                insert(edit.data);
                break;
            case RIGHT:
                cursor=cursor.prev;
                break;
            case LEFT:
                cursor=cursor.next;
                break;
            default:
                return;
        }
    }

    public void redo(){

    }

    public String print(){
        CharNode root = end.prev;
        StringBuilder output = new StringBuilder();
        if(cursor == end) output.insert(0,'|');
        while(root!=null){
            output.insert(0,root.ch);
            if(cursor==root){
                output.insert(0,'|');
            }
            root=root.prev;
        }
        return output.toString();
    }

}


public class DoubleLinkedListBasedApproach {
}
