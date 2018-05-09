package sample;

public class PageCount {

    private int count;
    private int lastPage;


    public int getCount(){
        return count;
    }
    public void ResetCount(){
        count = 1;
    }
    public void IncrementCount(){
        count++;
    }
    public void DecrementCount(){
        count--;
    }


    public int getLastPage(){
        return lastPage;
    }
    public void setLastPage(int num){
        lastPage = num;
    }
}
