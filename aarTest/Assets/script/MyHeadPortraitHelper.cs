using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MyHeadPortraitHelper : MonoBehaviour
{
    static MyHeadPortraitHelper instance;
    public static MyHeadPortraitHelper GetInstance(){
        if(instance==null){
            GameObject go = new GameObject("MyHeadPortraitHelper");
            DontDestroyOnLoad(go);
            instance = go.AddComponent<MyHeadPortraitHelper>(); 
        }
        return instance;
    }


    private AndroidJavaObject m_Jo;
    private AndroidJavaClass m_Jc;



    public void selectHeadPortrait() {
         
    }


}
