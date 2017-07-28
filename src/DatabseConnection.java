import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class DatabseConnection {

	Connection con;
	//CREATE CONNECTION WITH SQL
	public void CreateConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hackr","root","A@#$lll123");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//GETS THE LIST OF TUTORIALS FOR RESPECTIVE LANGUAGE
	public ResultSet getDefaultTutorialsList(int language) throws SQLException{
		
		CreateConnection();
		PreparedStatement ps2 = con.prepareStatement("select * from tutorials where lang_id = ?");
		ps2.setInt(1,language);
		ResultSet res = ps2.executeQuery();
		return res;
	}
	//GETS THE LIST OF ALL THE LANGUAGES
 	public ResultSet getAllLanguages() throws SQLException{
	
		CreateConnection();
		PreparedStatement ps;
		ResultSet r = null;
		ps = con.prepareStatement("Select * from languages order by Cname;");
		r = ps.executeQuery();
		return r;
	}
 	//GETS THE FILTERED LIST OF TUTORIALS FOR RESPECTIVE LANGUAGE
 	public ResultSet getFilteredTutorialsList(int language,boolean isFree,boolean isPaid, boolean isBeginner, boolean isIntermediate, boolean isAdvance) throws SQLException{
 		
 		PreparedStatement ps2;
 		int free,paid;
 		if(isFree)	free = 1;
 		else	free = 0;
 		if(isPaid)	paid = 1;
 		else	paid = 0;
 		if(!isBeginner && !isAdvance && !isIntermediate && !isFree && !isPaid){
 			ps2 = con.prepareStatement("select * from tutorials where lang_id = ?");
 			ps2.setInt(1, language);
 		}
 		else{
 			if(isBeginner){
 				if(isIntermediate){
 					if(isAdvance){
 						ps2 = con.prepareStatement("select * from tutorials where lang_id = ? and free = ? or paid = ?");
 					}
 					else{
 						ps2 = con.prepareStatement("select * from (select * from tutorials where lang_id = ? and free = ? or paid = ?) as tuts where beginner = 1 or intermediate = 1 or advanced = 0");
 					}
 				}
 				else{
 					if(isAdvance){
 						ps2 = con.prepareStatement("select * from (select * from tutorials where lang_id = ? and free = ? or paid = ?) as tuts where beginner = 1 or intermediate = 0 or advanced = 1");
 					}
 					else{
 						ps2 = con.prepareStatement("select * from (select * from tutorials where lang_id = ? and free = ? or paid = ?) as tuts where beginner = 1 or intermediate = 0 or advanced = 0");
 					}
 				}
 			}
 			else{
 				if(isIntermediate){
 					if(isAdvance){
 						ps2 = con.prepareStatement("select * from (select * from tutorials where lang_id = ? and free = ? or paid = ?) as tuts where beginner = 0 and intermediate = 1 and advanced = 1");
 					}
 					else{
 						ps2 = con.prepareStatement("select * from (select * from tutorials where lang_id = ? and free = ? or paid = ?) as tuts where beginner = 0 and intermediate = 1 and advanced = 0");
 					}
 				}
 				else{
 					if(isAdvance){
 						ps2 = con.prepareStatement("select * from (select * from tutorials where lang_id = ? and free = ? or paid = ?) as tuts where beginner = 0 and intermediate = 0 and advanced = 1");
 					}
 					else{
 						ps2 = con.prepareStatement("select * from tutorials where lang_id = ? and free = ? or paid = ?");
 					}
 				}
 			}
 			ps2.setInt(1,language);
 	 		ps2.setInt(2, free);
 	 		ps2.setInt(3, paid);
 		}
		ResultSet res = ps2.executeQuery();
 		return res;
 	}
 	//GETS THE LANGUAGE'S ID FROM LANGUAGE TABLE
 	public int getLanguageId(String language) throws SQLException{
 		int id;
 		CreateConnection();
		PreparedStatement ps = con.prepareStatement("select id from languages where Cname = ?");
		ps.setString(1, language);
		ResultSet r = ps.executeQuery();
		r.first();
		String _Lid = r.getObject(1).toString();
		id = Integer.parseInt(_Lid);
 		return id;
 	}
}
