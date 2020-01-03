const express=require('express');
const mongo = require('mongodb').MongoClient;
const app=express()
const router=express.Router();

const bcrypt=require('bcrypt')
//router.set('view-engine','ejs')
router.use(express.urlencoded({extended:false}))

app.get('/login',(req,res)=>{
    res.render('login.ejs')
})

app.get('/register',(req,res)=>{
    res.render('register.ejs')
})

router.post('/login',(req,res)=>{
    console.log(req.body.password)
})

var users=[]

router.post('/register',async(req,res)=>{
    try{
        const hashedpass=await bcrypt.hash(req.body.password,10)
        users.push({
            id:Date.now().toString(),
            name:req.body.name,
            email:req.body.email,
            password:hashedpass
        })
        console.log(users)
        res.redirect('/login')
    }
    catch{
        res.redirect('/register')
    }
})


module.exports=router;