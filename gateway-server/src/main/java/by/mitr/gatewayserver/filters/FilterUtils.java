package by.mitr.gatewayserver.filters;

//@Component
public class FilterUtils {
//
//    public static final String CORRELATION_ID = "x-b3-traceid";
//
//    public String getCorrelationId(HttpHeaders requestHeaders) {
//        if (requestHeaders.get(CORRELATION_ID) != null) {
//            List<String> header = requestHeaders.get(CORRELATION_ID);
//            return header.stream().findFirst().get();
//        } else {
//            return null;
//        }
//    }
//
//    public ServerWebExchange setCorrelationId(ServerWebExchange exchange,
//                                              String correlationId) {
//        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
//    }
//
//    public ServerWebExchange setRequestHeader(ServerWebExchange exchange,
//                                              String name, String value) {
//        return exchange.mutate().request(
//                        exchange.getRequest().mutate()
//                                .header(name, value)
//                                .build())
//                .build();
//    }
}
