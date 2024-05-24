package LLD.PubSub.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Message {
    private final String msg;
    public  Message(final String msg){
        this.msg = msg;
    }
}
