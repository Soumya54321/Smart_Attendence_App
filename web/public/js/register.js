var n;
var n1;

$('.form-check-input').on('change', function() {
    $('.form-check-input').not(this).prop('checked', false);  
});

function check() {
    if(document.getElementById("check1").checked){
        document.getElementById("register_form").style.visibility = "visible";
        document.getElementById("register_form1").style.visibility = "hidden";

    }
    if(document.getElementById("check2").checked){
        document.getElementById("register_form1").style.visibility = "visible";
        document.getElementById("register_form").style.visibility = "hidden";
        
    }
    
}

document.getElementById("register_form1").style.visibility = "hidden";
document.getElementById("register_form").style.visibility = "hidden";


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
        document.getElementById("exampleInputName").disabled=true;
        document.getElementById("exampleInputDepartment").disabled=true;
        document.getElementById("exampleInputYear").disabled=true;
        document.getElementById("exampleInputRoll").disabled=true;
        document.getElementById("exampleInputContact").disabled=true;
        document.getElementById("exampleInputEmail1").disabled=true;
        document.getElementById("exampleInputPassword1").disabled=true;
        document.getElementById("exampleInputPassword2").disabled=true;
    
    }
    else{
        alert("OTP Invalid");
        n=0;
    }


}
