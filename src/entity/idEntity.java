package entity;
//定义了一个实体类的父类，这是一个抽象类
public abstract class idEntity {

    protected String id;
    public String getid(){
        return id;
    }
    public void setid(String i){
        this.id=i;
    }
}
