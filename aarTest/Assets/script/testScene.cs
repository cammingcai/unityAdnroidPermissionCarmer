using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class testScene : MonoBehaviour
{

    private AndroidJavaObject m_Jo;
    private AndroidJavaClass m_Jc;

    public Image m_image;

    private Texture texture0;

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void click() {
        m_Jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        m_Jo = m_Jc.GetStatic<AndroidJavaObject>("currentActivity");
        m_Jo.Call("setHead");
    }

    public void onActivityResultCallback(string path) {
        Debug.Log("###  path: "+ path);

        StartCoroutine(loadLocal(path));

    }


    IEnumerator loadLocal(string path)
    {
        //if (texture0 == null)
        //{
        //    //Debug.Log("## Application.dataPath: "+ Application.dataPath);
        //    //资源在本地的路径上
        //    //WWW date = new WWW("file://" + Application.dataPath + "/muzico.jpg");
        //    //WWW date = new WWW(path);
        //    WWW date = new WWW("file://" + Application.persistentDataPath + "/image.jpg");
        //    //等待下载完
        //    yield return date;
        //    //下载完，得到所下载的图像的贴图
        //    texture0 = date.texture;
        //}
        ////更换为下载的贴图
        //Renderer render = m_image.GetComponent<Renderer>();
        //render.material.mainTexture = texture0;


        Debug.Log("## Application.persistentDataPath: " + Application.persistentDataPath);
        WWW www = new WWW("file://" + Application.persistentDataPath + "/image.jpg");
        yield return www;
        if (string.IsNullOrEmpty(www.error))
        {
            Texture2D tex = www.texture;
            Sprite temp = Sprite.Create(tex, new Rect(0, 0, tex.width, tex.height), new Vector2(0, 0));
            m_image.sprite = temp;
        }
        else {
            Debug.Log("## IsNullOrEmpty");
        }
    }


}
