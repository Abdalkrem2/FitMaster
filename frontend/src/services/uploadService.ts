import { api } from "./api";
 
 
 
 export const uploadService = {

uploadImage:async(file:File)=>{
    const formDataImg = new FormData();
      formDataImg.append("file", file);

      const uploadRes = await api.post("/files/upload", formDataImg);
      return uploadRes.data;
} 


    }