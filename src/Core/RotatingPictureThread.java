package Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import mediautil.image.jpeg.LLJTran;
import mediautil.image.jpeg.LLJTranException;

public class RotatingPictureThread extends Thread {
	
	String filePath;
	int specifyRotation;
	public RotatingPictureThread(String filePath, int specifyRotation)
	{
		super();
		this.filePath = filePath;
		this.specifyRotation = specifyRotation;
	}
	
	public void run() 
	{
		try {
			File sourceImageFile = new File(filePath);
			File rotatedImageFile = new File(filePath);
			FileInputStream inputStream = new FileInputStream(sourceImageFile);
			LLJTran jt = new LLJTran(inputStream);
			jt.read(LLJTran.READ_ALL,true);
			if(specifyRotation == 90)
			{
				jt.transform(LLJTran.ROT_90);
			}
			else {
				jt.transform(LLJTran.ROT_270);
			}
			FileOutputStream outputStream = new FileOutputStream(rotatedImageFile);
			jt.save(outputStream, LLJTran.OPT_WRITE_ALL);
			outputStream.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (LLJTranException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("rotated");
	}
}
