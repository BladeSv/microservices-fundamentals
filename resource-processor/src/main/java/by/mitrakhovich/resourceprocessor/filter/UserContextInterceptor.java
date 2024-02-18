package by.mitrakhovich.resourceprocessor.filter;

//@Component
//@Slf4j

public class UserContextInterceptor {
}

//public class UserContextInterceptor implements ClientHttpRequestInterceptor {
//
//
//    @Override
//    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//        HttpHeaders headers = request.getHeaders();
//        headers.add(UserContext.CORRELATION_ID,
//                UserContextHolder.getContext().
//                        getCorrelationId());
//        headers.add(UserContext.AUTH_TOKEN,
//                UserContextHolder.getContext().
//                        getAuthToken());
//        return execution.execute(request, body);
//
//    }
//}
