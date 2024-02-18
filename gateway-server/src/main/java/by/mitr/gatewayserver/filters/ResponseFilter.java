package by.mitr.gatewayserver.filters;

//@Configuration
public class ResponseFilter {
//    final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);
//
//    @Autowired
//    FilterUtils filterUtils;
//
//    @Bean
//    public GlobalFilter postGlobalFilter() {
//        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
//            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
//            String correlationId =
//                    filterUtils.
//                            getCorrelationId(requestHeaders);
//            logger.info(
//                    "Adding the correlation id to the outbound headers. {}",
//                    correlationId);
//            exchange.getResponse().getHeaders().
//                    add(FilterUtils.CORRELATION_ID,
//                            correlationId);
//            logger.info("Completing outgoing request for {}.", exchange.getRequest().getURI());
//        }));
//    }
}
