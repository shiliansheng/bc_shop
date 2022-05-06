 

function createxmlhttp()
{
     var activeKey=new Array("MSXML2.XMLHTTP.5.0",
                           "MSXML2.XMLHTTP.4.0",
                           "MSXML2.XMLHTTP.3.0",
                           "MSXML2.XMLHTTP",
                           "Microsoft.XMLHTTP");
    if(window.ActiveXObject)
    {
        for(var i=0;i<activeKey.length;i++)
        {
            try
            {
                xmlHttp=new ActiveXObject(activeKey[i]);
                if(xmlHttp!=null)
                    return xmlHttp;
            }
            catch(error)
            {
                 continue;
            } 
        }
        throw new Error("客户端浏览器版本过低，不支持XMLHttpRequest对象，请更新浏览器");
    }
    else if(window.XMLHttpRequest)
    {
		//alert("3");
        xmlHttp=new window.XMLHttpRequest();
    }
	return xmlHttp;
}



function get_j2(quanxian1_id){
	alert(quanxian1_id+"00");
	if(quanxian1_id==0){
		document.getElementById("subclass").innerHTML="<select name='smallclassid'><option value='0' selected>二级分类</option></select>";
		return;
	};
	var xmlhttpobj = createxmlhttp();
	if(xmlhttpobj){//如果创建对象xmlhttpobj成功
		//alert(bigclassid);
		xmlhttpobj.open('get',"/get_j2?quanxian1_id="+quanxian1_id+"&number="+Math.random(),true);//get方法 加个随机数。
		xmlhttpobj.send(null);		
		xmlhttpobj.onreadystatechange=function(){//客户端监控函数
		//alert(xmlhttpobj.responseText);	
		var html = xmlhttpobj.responseText;//获得返回值	
 			if(xmlhttpobj.readystate==4){//服务器处理请求完成
				if(xmlhttpobj.status==200){
					//alert('ok');
					document.getElementById("subclass").innerHTML=html;
				}else{
					document.getElementById("subclass").innerHTML=html;
				}
			}else{
				document.getElementById("subclass").innerHTML=html;//服务器处理中
			}
		}	
	}
}

function get_j3(bigclassid){
	//document.getElementById("j3").innerHTML="aaaaaaa";
	//alert("33" + bigclassid);
	if(bigclassid==0){
		//document.getElementById("subclass").innerHTML="<font >选择二级分类</option></select>";
		document.getElementById("if_j3").src ="1.jsp";
		//return;
	}
	else
	{
		document.getElementById("if_j3").src ="3.jsp?int_jb2_id="+bigclassid ;
	}
	
}
