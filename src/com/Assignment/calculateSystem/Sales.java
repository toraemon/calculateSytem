package com.Assignment.calculateSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Sales {
	private String fileName;
	private String branchCode;
	private String commodityCode;
	private long sales;
	
	public Sales(String fileName, String branchCode, String commodityCode, long sales){
		this.fileName = fileName;
		this.branchCode = branchCode;
		this.commodityCode = commodityCode;
		this.sales = sales;
	}
	public String getFileName(){
		return fileName;
	}
	public String getBranchCode(){
		return branchCode;
	}
	public String getCommodityCode(){
		return commodityCode;
	}
	public long getSales(){
		return sales;
	}
	
	/**
	 * 8桁の数字で拡張子がrcdのファイルをListに格納するメソッド。
	 * @param path
	 * @return：条件に該当するrcdファイル
	 */
	static List<File> searchSalesFile(String path){
		File directory = new File(path);
		File[] files = directory.listFiles();
		List<File> fileList = new ArrayList<>();
		for(int i=0;i<files.length;i++){
			if(files[i].getName().matches("\\d{8}.rcd")){
				fileList.add(files[i]);
			}
		}
		// 売上ファイルが連番になっているかチェック
		serialNumberCheck(path, fileList);
		return fileList;
	}
	
	/**
	 * 売上ファイルが連番になっていない場合処理を中断させるメソッド
	 * @param path
	 * @param fileList
	 */
	static void serialNumberCheck(String path, List<File> fileList){
		int[] rcdNumber = new int[fileList.size()];
		for(int i=0;i<fileList.size();i++){
			rcdNumber[i] = Integer.parseInt(fileList.get(i).getName().substring(0, 8));
			if(fileList.size()<rcdNumber[i]){
				System.err.println("売上ファイルが連番になっていません。\n処理を終了します。");
				System.exit(1);
			}
		}
	}
	
	/**
	 * @param fileList
	 * @return
	 * @throws IOException
	 */
	static List<Sales> readSalesFile(List<File> fileList) throws IOException{
		List<Sales> salesList = new ArrayList<>();
		BufferedReader br = null;
		try{
			for(int i=0;i<fileList.size();i++){
				br = new BufferedReader(new FileReader(fileList.get(i)));
				String[] line = new String[fileList.size()];
				int j = 0;
				while((line[j]=br.readLine())!=null){
					j++;
				}
				Sales sales = new Sales(fileList.get(i).getName(), line[0], line[1], Long.parseLong(line[2]));
				salesList.add(sales);
			}
		}catch(IOException | ArrayIndexOutOfBoundsException e){
			System.err.println(e);
			e.printStackTrace();
		}finally{
			br.close();
		}
		return salesList;
	}
}