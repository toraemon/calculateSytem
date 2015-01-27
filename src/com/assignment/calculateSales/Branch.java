package com.assignment.calculateSales;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Branch extends Definition{
	private final String DEF_FILE_NAME = "branch";
	private List<Branch> list = new ArrayList<>();
	
	public Branch(){}
	private Branch(String code, String name){
		super.setCode(code);
		super.setName(name);
	}
	
	public String getName(){
		return "支店";
	}
	public List<Branch> getList(){
		return list;
	}
	
	public void readDefinitionFile(String path) throws IOException{
		super.readDefinitionFile(path, DEF_FILE_NAME);
	}
	
	@Override
	boolean checkFormat(int num, String lineList){
		if(num==0 && !lineList.matches("\\d{3}")){
			return false;
		}
		return true;
	}
	
	@Override
	void inputList(String[] lineList){
		Branch branch = new Branch(lineList[0], lineList[1]);
		list.add(branch);
	}
	
	@Override
	public void calculateSales(List<Sales> readSales){
		final long MAX_RANGE = 1_000_000_000L;
		for(int i=0;i<readSales.size();i++){
			int branchFlg = 0;
			int branchSuffix = -1;
			for(int j=0;j<list.size();j++){
				if(list.get(j).getCode().equals(readSales.get(i).getBranchCode())){
					branchFlg = 1;
					branchSuffix = j;
				}
			}
			if(branchFlg==1){
				list.get(branchSuffix).calculate(readSales.get(i).getSales());
				if(MAX_RANGE<=list.get(branchSuffix).getSales()){
					System.err.println("合計金額が10桁を越えました。処理を終了します。");
					System.exit(1);
				}
			}else if(branchFlg==0){
				System.err.println(readSales.get(i).getFileName()+"の支店コードが不正です。");
			}
		}
	}
	
	/**
	 * 集計したファイルを書き出すメソッド
	 * @param path
	 * @param name
	 * @throws IOException
	 */
	void writeOutputFile(String path) throws IOException{
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(path+"/"+DEF_FILE_NAME+".out"));
			for(int i=0;i<list.size();i++){
				bw.write(list.get(i).getCode()+","
						+list.get(i).getName()+","
						+list.get(i).getSales()+"\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			bw.close();
		}
	}
}