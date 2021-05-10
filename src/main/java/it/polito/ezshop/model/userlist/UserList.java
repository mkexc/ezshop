package it.polito.ezshop.model.userlist;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.model.customerlist.Customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class UserList {
    private List<User> userList;

    public UserList (){
        userList = new ArrayList<User>();
    }

    public void add(User u){
        userList.add(u);
    }

    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        //userList.add(new User(
           //userList.size()+1,username,password,role,false
        //));
        return 0;
    }

    public boolean deleteUser(Integer id) throws InvalidUserIdException{
        if (id==null || id.intValue()<=0)
            throw new InvalidUserIdException();
        for ( User u: userList) {
            if (u.getId().intValue() == id.intValue())
            {
                userList.remove(u);
                return true;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        return this.userList;
    }

    public User getUser (Integer id){
        Iterator<User> it=userList.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if(u.getId().intValue() == id.intValue())
            {
                return u;
            }
        }
        return null;
    }

    public boolean updateUserRights(Integer id, String role){
        Iterator<User> it=userList.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if(u.getId().intValue() == id.intValue())
            {
                u.setRole(role);
                return true;
            }
        }
        return false;

    }

    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if (username==null || username.isEmpty())
        {
            throw new InvalidUsernameException();
        }
        else if (password==null || password.isEmpty())
        {
            throw new InvalidPasswordException();
        }
        else
        {
            User u = userList.stream().filter(x-> x.getUsername().equals(username) && x.getPassword().equals(password)).collect(Collectors.toList()).get(0);
            if (u != null)
                //u.setStatus(true);
                return u;
            return u;
        }
    }

    public String toString(){
        //String res="";
        //for(User u : userList){
          //  res+="name: " + u.getUsername() + "pwd: "+ u.getPassword() +"\n" ;
        //}
        return "";
    }

    public void logout(){

    }

    public void savePersistent(){

    }
}
