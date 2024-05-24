//// create/read/update/delete/begin/commit/rollabck/commit(t)/rollback(t)
//// nested transactions!
//
//// Implementation 1 - create/read/update/delete + commit/rollback + nested transaction        (no commit(t) or rollback(t))


//// **IMP**
//// in case of NO nested transaction easy simulation can be done just using these -
//
////     private Map<String, String> globalStore;
////     private Map<String, String> intStore;
////     private boolean transactionOn;
//
//


//#include<bits/stdc++.h>
//        using namespace std;
//
//        struct Transaction{
//        unordered_map<string, string> store;
//        Transaction* next;
//
//        Transaction(){
//        next = NULL;
//        }
//        };
//
//        struct TransactionStack{
//        Transaction *top;
//        int size;
//
//        TransactionStack(){
//        top= NULL;
//        size = 0;
//        }
//        };
//
//        TransactionStack ts;
//        unordered_map<string, string> globalStore;
//
//        void begin(){
//        Transaction* temp = new Transaction();
//        temp->next = ts.top;
//        ts.top = temp;
//        ts.size++;
//        }
//
//        void commit(){
//        Transaction* active = ts.top;
//        if(active!=NULL){
//        for(auto it: active->store){
//        string key = it.first;
//        string val = it.second;
//        if(""==val && globalStore.find(key)!=globalStore.end()){
//        globalStore.erase(key);
//        }else{
//        globalStore[key] = val;
//        }
//        // update the parent
//        if(active->next!=NULL){
//        active->next->store[key] = val;
//        }
//        }
//        // pop transaction from the stack
//        ts.top = ts.top->next;
//        ts.size--;
//        }
//        }
//
//        void rollback(){
//        Transaction* active = ts.top;
//        if(active!=NULL){
//        ts.top->store.clear();
//
//        // pop transaction from stack
//        ts.top = ts.top->next;
//        ts.size--;
//        }
//        }
//
//        void settj(string key, string val){
//        Transaction* active = ts.top;
//        if(active==NULL){
//        globalStore[key] = val;
//        }else{
//        active->store[key] = val;
//        }
//        }
//
//// in get we can have a variation if key is not present in active transaction but still we want the last update that happened maybe in a previous transaction that is still going on or maybe the one present in a global store//
//// global store is easy but if we want value from a previous transaction which was not commited yet it will be really tricky.. iterate through all the parents and check if key is present
//// we can also have a cache like structure for that key -> linkedlist of updates
//// but that would be really tricky
//
//// we are currently implementing, if the key is not present in the current transaction, return the one present in global stack if present
//        string get(string key){
//        Transaction* active = ts.top;
//        if(active==NULL){
//        if(globalStore.find(key)==globalStore.end()){
//        cout<<key<<" "<<"Error"<<endl;
//        return "Error";
//        }
//
//        cout<<key<<" "<<globalStore[key]<<endl;
//        return globalStore[key];
//        }else{
//        if(active->store.find(key)==active->store.end()){
//        if(globalStore.find(key)==globalStore.end()){
//        cout<<key<<" "<<"Error"<<endl;
//        return "Error";
//        }
//
//        cout<<key<<" "<<globalStore[key]<<endl;
//        return globalStore[key];
//        }
//
//
//        cout<<key<<" "<<active->store[key]<<endl;
//        return active->store[key];
//        }
//        }
//
//        void deletetj(string key){
//        Transaction* active = ts.top;
//        if(active==NULL){
//        if(globalStore.find(key)!=globalStore.end())
//        globalStore.erase(key);
//        }else{
//        active->store[key] = "";
//        }
//        }
//
//
//        int main() {
//        begin();
//        settj("tj","1");
//        get("tj");
//        settj("tj","2");
//        get("tj");
//        commit();
//
//        begin();
//        settj("tj","3");
//        get("tj");
//        rollback();
//
//        get("tj");
//
//        //nested
//        begin();
//        settj("tj","4");
//        settj("tj2","1");
//        get("tj2");
//        begin();
//        settj("tj2","5");
//        get("tj2");
//        rollback();
//        commit();
//
//        get("tj2");
//        get("tj");
//
//        //nested delete
//        begin();
//        deletetj("tj2");
//        begin();
//        deletetj("tj");
//        rollback();
//        commit();
//
//        get("tj");
//        get("tj2");
//
//
//
//        }