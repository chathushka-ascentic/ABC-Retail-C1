package abc.ap.com.abcfashions.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import abc.ap.com.abcfashions.model.Branch;
import abc.ap.com.abcfashions.model.Users;


public class DataService
{
	private Context context;

	public DataService(Context context)
	{
		this.context=context;
	}

	//user
	public boolean Login(Users users) throws Exception
	{
			boolean isLoginSucess = false;

			Cursor cursor = DatabaseHelper.getInstance(context).ExecuteSql("SELECT id,fName,contactNo1,contactNo2,email,address FROM users");

			if (cursor.moveToNext())
			{
				isLoginSucess = true;
				if (users != null)
				{
                    users.setUserId(cursor.getInt(0));
                    users.setFullname(cursor.getString(1));
                    users.setContactNo1(cursor.getString(2));
					users.setContactNo2(cursor.getString(3));
					users.setEmail(cursor.getString(4));
					users.setAddress(cursor.getString(5));
				}
			}
			cursor.close();

			return isLoginSucess;
	}

	public void insertUser(Users users) {

		try {

			ContentValues contentValues = new ContentValues();
			contentValues.put("id", users.getUserId());
			contentValues.put("fName", users.getFullname());
			contentValues.put("contactNo1", users.getContactNo1());
			contentValues.put("contactNo2", users.getContactNo2());
			contentValues.put("email", users.getEmail());
			contentValues.put("address", users.getAddress());

			DatabaseHelper.getInstance(context).Insert("users", contentValues);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void updateUsersTable(Users users) throws Exception {

		ContentValues values = new ContentValues();

		values.put("fName", users.getFullname());
		values.put("contactNo1", users.getContactNo1());
		values.put("contactNo2", users.getContactNo2());
		values.put("address", users.getAddress());
		DatabaseHelper.getInstance(context).Update("users", values, "id =?", new String[]{String.valueOf(users.getUserId())});

	}


	//branch
	public ArrayList<Branch> getList() throws Exception
	{
		ArrayList<Branch> branches =  new ArrayList<>();
		Cursor cursor = DatabaseHelper.getInstance(context).ExecuteSql("SELECT branchId,branchName,latitude,longitude,address,contactNo FROM branch");

		while (cursor.moveToNext())
		{
			Branch branch = new Branch();

			branch.setBranchId(cursor.getInt(0));
			branch.setBranchName(cursor.getString(1));
			branch.setLatitude(cursor.getDouble(2));
			branch.setLongitude(cursor.getDouble(3));
			branch.setAddress(cursor.getString(4));
			branch.setContactNo(cursor.getString(5));
			branches.add(branch);
		}
		cursor.close();

		return branches;
	}


	//cleardb
	public void ClearDb() {

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					DatabaseHelper.getInstance(context).Delete("users");
					DatabaseHelper.getInstance(context).Delete("branch");
				}
				catch (Exception e){e.printStackTrace();}
			}}).start();

	}
}
