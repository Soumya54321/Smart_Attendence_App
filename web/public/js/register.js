var n;
var n1;


function limit(){
    var str1;
    
    str1=prompt("Enter the OTP");
    while(str1.length == 0 || !(str1.charCodeAt(0)>47 && str1.charCodeAt(0)<58)){
        str1= prompt("Enter the OTP");     
    }
    n=parseInt(str1);
    mFunction();//checking function to check whether given otp is same.
}



function mFunction() {

    n1=1234;

    if(n==n1){
        
        document.getElementById("abc").style.visibility = "hidden";
        document.getElementById("abcd").style.visibility = "visible";
        document.getElementsByTagName("INPUT").disabled=true;
    
    }
    else{
        alert("OTP Invalid");
        n=0;
    }


}
