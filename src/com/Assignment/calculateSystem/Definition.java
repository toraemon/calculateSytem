package com.Assignment.calculateSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

abstract public class Definition {
	private String code;
	private String name;
	private long sales;
	
	public String getCode(){
		return code;
	}
	public void setCode(String code){
		this.code = code;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public long getSales(){
		return sales;
	}

	/**
	 * 売上を加算するメソッド
	 * @param sales
	 */
	public void calculate(long sales){
		this.sales += sales;
	}

	/**
	 * 定義ファイルを読み込むメソッド。
	 * 定義ファイルフォーマットが不正、定義ファイルがない場合はエラーを返す
	 * @param path：コマンドライン引数
	 * @param name：定義ファイルの名前
	 * @throws IOException
	 */
	protected void readDefinitionFile(String path, String name) throws IOException{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(path+"/"+name+".lst"));
			String line;
			String[] lineList;

			while((line=br.readLine())!=null){
				lineList = line.split(",");
				for(int i=0;i<lineList.length;i++){
					if(!checkFormat(i, lineList[i])){
						System.err.println(getName()+"定義ファイルフォーマットが不正です。\n処理を終了します。");
						System.exit(1);
					}
				}
				inputList(lineList);
			}
		}catch(FileNotFoundException e){
			System.err.println(getName()+"定義ファイルがありません。\n処理を終了します。");
			System.exit(1);
		}catch(IOException e){
			System.exit(1);
		}finally{
			br.close();
		}
	}
	abstract boolean checkFormat(int num, String lineList);
	abstract void inputList(String[] lineList);
	abstract void calculateSales(List<Sales> readSales);
}