package com.rainfir.server;

public class TestStatic {
    //普通属性、方法
    private String attr = "这是普通属性";
    private void nomalWay(){
        System.out.println("这是普通方法");

        //普通方法访问静态属性
        System.out.println(staticAttr);
        //普通方法访问普通属性
        System.out.println(attr);
    }
    //静态属性、方法
    static private String staticAttr = "这是静态属性";
    static private void staticWay(){
        System.out.println("这是静态方法");
    }
    public static void main(String[] args) {
        //普通属性方法调用,只能通过引用（创建对象）来访问
        TestStatic testStatic = new TestStatic();
        System.out.println(testStatic.attr);
        testStatic.nomalWay();
        //静态属性方法调用，既可以通过类名来访问，也可以通过引用名来访问
        //1 通过类名访问
        TestStatic.staticWay();
        System.out.println(TestStatic.staticAttr);
        //2 通过引用名
        System.out.println(staticAttr);
        staticWay();
    }
}
