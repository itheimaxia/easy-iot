package com.itheimaxia.easyiotgateway.core.loadbalance;

import java.util.List;

public interface LoadBalancer {
    String chooseDestination(List<String> destnations);
}
