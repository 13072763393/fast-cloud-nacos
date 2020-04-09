package fast.cloud.nacos.stomp.websocket.service.impl;

import fast.cloud.nacos.stomp.websocket.service.IRedisSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Classname SimulationRedisSessionServiceImpl
 * @Description TODO
 * @Date 2020/4/9 15:28
 * @Created by qinfuxiang
 */
@Component
public class SimulationRedisSessionServiceImpl implements IRedisSessionService {

    @Autowired
    private RedisTemplate<String, String> template;


    // key = 登录用户名称， value=websocket的sessionId
    private ConcurrentHashMap<String,String> redisHashMap = new ConcurrentHashMap<>(32);

    /**
     * 在缓存中保存用户和websocket sessionid的信息
     * @param name
     * @param wsSessionId
     */
    @Override
    public void add(String name, String wsSessionId){
        BoundValueOperations<String,String> boundValueOperations = template.boundValueOps(name);
        boundValueOperations.set(wsSessionId,24 * 3600, TimeUnit.SECONDS);
    }

    /**
     * 从缓存中删除用户的信息
     * @param name
     */
    @Override
    public boolean del(String name){
        return template.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] rawKey = template.getStringSerializer().serialize(name);
                return connection.del(rawKey) > 0;
            }
        }, true);
    }

    /**
     * 根据用户id获取用户对应的sessionId值
     * @param name
     * @return
     */
    @Override
    public String get(String name){
        BoundValueOperations<String,String> boundValueOperations = template.boundValueOps(name);
        return boundValueOperations.get();
    }
}
