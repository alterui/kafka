package com.lr.kafka.admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author liurui
 * @date 2021/4/10 17:26
 */
public class AdminApplication {

    private final static String TOPIC_NAME = "lr-topic";

    public static void main(String[] args) throws Exception{
       // createTopic();
        delTopics();
        getTopicLists();
    }

    /**
     * 获取topic list
     * @throws Exception
     */
    public static void getTopicLists() throws Exception {
        AdminClient adminClient = getAdminClient();
        //是否查看internal选项
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);

        //ListTopicsResult listTopicsResult = adminClient.listTopics();
        ListTopicsResult listTopicsResult = adminClient.listTopics(listTopicsOptions);

        listTopicsResult.names().get().forEach(System.out::println);
    }


    /**
     * 创建topic实例
     */
    public static void createTopic() {
        AdminClient adminClient = getAdminClient();
        //副本因子
        short rs = 1;
        NewTopic topic = new NewTopic(TOPIC_NAME, 1, rs);
        adminClient.createTopics(Collections.singletonList(topic));

    }

    /**
     * 设置adminServer
     *
     * @return
     */
    public static AdminClient getAdminClient() {
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.47.129:9092");
        return AdminClient.create(properties);
    }

    /**
     * 删除topic
     */
    public static void delTopics() {
        AdminClient adminClient = getAdminClient();
        adminClient.deleteTopics(Collections.singletonList(TOPIC_NAME));

    }

    /**
     * 描述topic
     * @throws Exception
     */
    @Test
    public void describeTopics() throws Exception{
        AdminClient adminClient = getAdminClient();
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singletonList(TOPIC_NAME));
        Map<String, TopicDescription> stringTopicDescriptionMap = describeTopicsResult.all().get();
        stringTopicDescriptionMap.forEach((k,v)->{
            System.out.println("k:" + k + ",v:" + v);
        });
    }


    /**
     * 描述topic
     * @throws Exception
     */
    @Test
    public void describeConfig() throws Exception{
        AdminClient adminClient = getAdminClient();
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(configResource));
        Map<ConfigResource, Config> configResourceConfigMap = describeConfigsResult.all().get();
        configResourceConfigMap.forEach((k,v)->{
            System.out.println("k:" + k + ",v:" + v);
        });
    }

    /**
     * 修改Config信息
     */
    @Test
    public void updateTopicConfig() {
        AdminClient adminClient = getAdminClient();

        Map<ConfigResource, Config> configMaps = new HashMap<>();

        //组织两个参数
        //1.修改哪个TOPIC
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        //2.修改哪个Config
        Config config = new Config(Collections.singleton(new ConfigEntry("preallocate", "true")));

        configMaps.put(configResource, config);
        adminClient.alterConfigs(configMaps);

    }


}
