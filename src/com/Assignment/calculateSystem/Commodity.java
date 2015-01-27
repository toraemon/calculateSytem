package com.Assignment.calculateSystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Commodity extends Definition{
	private final String DEF_FILE_NAME = "commodity";
	private List<Commodity> list = new ArrayList<>();
	
	public Commodity(){}
	private Commodity(String code, String name){
		super.setCode(code);
		super.setName(name);
	}
	
	public String getName(){
		return "商品";
	}
	public List<Commodity> getList(){
		return list;
	}
	
	public void readDefinitionFile(String path) throws IOException{
		super.readDefinitionFile(path, DEF_FILE_NAME);
	}
	
	@Override
	boolean checkFormat(int num, String lineList) {
		if(num==0 && !lineList.matches("[0-9a-zA-Z]{8}")){
			return false;
		}
		return true;
	}
	
	@Override
	void inputList(String[] lineList) {
		Commodity commodity = new Commodity(lineList[0], lineList[1]);
		list.add(commodity);
	}
	
	@Override
	public void calculateSales(List<Sales> readSales){
		final long MAX_RANGE = 1_000_000_000L;
		for(int i=0;i<readSales.size();i++){
			int commodityFlg = 0;
			int commoditySuffix = -1;
			for(int j=0;j<list.size();j++){
				if(list.get(j).getCode().equals(readSales.get(i).getCommodityCode())){
					commodityFlg = 1;
					commoditySuffix = j;
				}
			}
			if(commodityFlg==1){
				list.get(commoditySuffix).calculate(readSales.get(i).getSales());
				if(MAX_RANGE<=list.get(commoditySuffix).getSales()){
					System.err.println("合計金額が10桁を越えました。処理を終了します。");
					System.exit(1);
				}
			}else if(commodityFlg==0){
				System.err.println(readSales.get(i).getFileName()+"の支店コードが不正です。");
			}
		}
	}
	
	/**
	 * 集計したデータをファイルに書き出すメソッド
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