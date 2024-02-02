package com.dev.service.implementation;

import java.util.Optional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.dev.model.message.Message;
import com.dev.model.message.user.UserMess;
import com.dev.model.user.User;
import com.dev.repository.MessageRepository;
import com.dev.repository.UserRepository;
import com.dev.service.UserService;
import com.dev.utils.TokenUtils;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public User loginUser(User user) {
        String mail = user.getMail();
        String mdp = user.getMotdepasse();
        // test statique
        ArrayList<User> data = statiqueData();
        for(User u : data){
            if( (u.getMail().equalsIgnoreCase(mail)==true) && (u.getMotdepasse().equalsIgnoreCase(mdp)==true) ){
                return u;
            }
        }
        return null;
    }

    // Ajout donnée statique 
    public static ArrayList<User> statiqueData(){
        ArrayList<User> data = new ArrayList<>();
        // data.add(new User(1,"Jean","Doe","mail1","pass1"));
        // data.add(new User(2,"Marc","Doe","mail2","pass2"));
        return data;
    }

    @Override
    public Message envoyerMessage(User userSend, User userReceive, String contenu, Timestamp dateTime)
    throws Exception {
        UserMess send=new UserMess();
        send.setId(userSend.getId());
        send.setNom(userSend.getNom());
        send.setPrenom(userSend.getPrenom());
        send.setMail(userSend.getMail());
        UserMess receive=new UserMess();
        receive.setId(userReceive.getId());
        receive.setNom(userReceive.getNom());
        receive.setPrenom(userReceive.getPrenom());
        receive.setMail(userReceive.getMail());
        // java.util.Date utilDate = new java.util.Date();
        // Timestamp timestamp = new Timestamp(utilDate.getTime());
        Message message=new Message(null, send, receive, contenu, 1, new java.util.Date());
        messageRepository.save(message);
        return message;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByMail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

	@Override
	public List<Message> findByUserSendAndUserReceive(User userSend, User userReceive) {
        UserMess send=new UserMess();
        send.setId(userSend.getId());
        send.setNom(userSend.getNom());
        send.setPrenom(userSend.getPrenom());
        send.setMail(userSend.getMail());
        UserMess receive=new UserMess();
        receive.setId(userReceive.getId());
        receive.setNom(userReceive.getNom());
        receive.setPrenom(userReceive.getPrenom());
        receive.setMail(userReceive.getMail());
        Criteria criteria = new Criteria().orOperator(
            Criteria.where("userSend").is(send).and("userReceive").is(receive),
            Criteria.where("userSend").is(receive).and("userReceive").is(send)
        );

        Query query = new Query(criteria).with(Sort.by(Sort.Order.asc("dateHeureMessage")));

        return mongoTemplate.find(query, Message.class);
        // return messageRepository.findByUserSendAndUserReceiveOrUserReceiveAndUserSendOrderByDateHeureMessage(userSend, userReceive, userReceive, userSend);
	}

    @Override
    public List<UserMess> findDistinctUsersForUser(User user) {
        UserMess connected=new UserMess();
        connected.setId(user.getId());
        connected.setNom(user.getNom());
        connected.setPrenom(user.getPrenom());
        connected.setMail(user.getMail());
        Query query = new Query(Criteria.where("userSend").is(connected));
        List<Message> userSendList = mongoTemplate.find(query, Message.class);
        Query query2 = new Query(Criteria.where("userReceive").is(connected));
        List<Message> userReceive = mongoTemplate.find(query2, Message.class);
        HashMap<Integer, UserMess> users=new HashMap<>();
        List<UserMess> result=new ArrayList<>();
        for(int i=0; i<userSendList.size(); i++) {
            if(users.get(userSendList.get(i).getUserReceive().getId())==null&&userSendList.get(i).getUserReceive().getId()!=connected.getId()) {
                users.put(userSendList.get(i).getUserReceive().getId(), userSendList.get(i).getUserReceive());
                result.add(userSendList.get(i).getUserReceive());
            }
        }
        for(int i=0; i<userReceive.size(); i++) {
            if(users.get(userReceive.get(i).getUserSend().getId())==null&&userReceive.get(i).getUserSend().getId()!=connected.getId()) {
                users.put(userReceive.get(i).getUserSend().getId(), userReceive.get(i).getUserSend());
                result.add(userReceive.get(i).getUserSend());
            }
        }
        return result;
    }
}
