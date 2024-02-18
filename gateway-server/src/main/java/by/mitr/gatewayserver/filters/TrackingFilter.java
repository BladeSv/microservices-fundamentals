package by.mitr.gatewayserver.filters;

//@Order(1)
//@Component
public class TrackingFilter {
}
//public class TrackingFilter implements GlobalFilter {
//
//    private static final Logger logger =
//            LoggerFactory.getLogger(TrackingFilter.class);
//    FilterUtils filterUtils;
//
//    public TrackingFilter(FilterUtils filterUtils) {
//        this.filterUtils = filterUtils;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        HttpHeaders requestHeaders =
//                exchange.getRequest().getHeaders();
//        if (isCorrelationIdPresent(requestHeaders)) {
//            logger.info(
//                    "tmx-correlation-id found in tracking filter: {}. ",
//                    filterUtils.getCorrelationId(requestHeaders));
//        } else {
//            String correlationID = generateCorrelationId();
//            exchange = filterUtils.setCorrelationId(exchange,
//                    correlationID);
//            logger.info(
//                    "tmx-correlation-id generated in tracking filter: {}.",
//                    correlationID);
//        }
//        return chain.filter(exchange);
//    }
//
//    private boolean isCorrelationIdPresent(
//            HttpHeaders requestHeaders) {
//        return filterUtils.getCorrelationId(requestHeaders) != null;
//    }
//
//    private String generateCorrelationId() {
//        return java.util.UUID.randomUUID().toString();
//    }
//}
