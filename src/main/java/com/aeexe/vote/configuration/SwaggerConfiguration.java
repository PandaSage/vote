package com.aeexe.vote.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

/**
 * 스웨거 설정
 * https://www.javaguides.net/2018/10/spring-boot-2-restful-api-documentation-with-swagger2-tutorial.html - guide
 * https://swagger.io/specification/					- specification
 * https://springfox.github.io/springfox/docs/current/	- documentation
 * https://springboot.tistory.com/24	- 기본설정
 *
 * https://yookeun.github.io/java/2017/02/26/java-swagger/ -추후 이쪽 설정도 참조하면 괜찮을거같음
 *
 * TODO : (2020.05.17 완료 : Pom.xml에서 exclusion 처리 및 버전 변경) 현재 Type이 숫자인 파라미터를 가져올 때 https://github.com/springfox/springfox/issues/2265 NumberFormatException이 나오는 문제가 있다.
 * 실행에는 아무 문제 없으나 확인은 필요
 * 추후 해당 html에서는 swagger을 적용하지 않거나 해당 페이지 접속에 대한 보안처리가 필요할 것 같으며
 * GraphQL은 API의 기조 자체가 변경되는 부분이 많아 아직 여기서 쓰기는 이를 것이라 생각된다.
 *
 *
 * 테스트 툴 및 깔끔한 주석등의 필요성이 필요하다고 생각되어 사용
 * @EnableSwagger2 - 설정한 스웨거를 사용하겠다는것을 명시, 없으면 직접 반영된 swagger에 대한 ui설정을 따로 해주어야 한다.
 * @author 82102
 */
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {
    private Contact contact;
    private ApiInfo info;
    //TODO : (2020.06.01 완료 : 외부 jar처리 없이 현재 이 config버전으로 관리ㄴ)저번 Config를 한번 더 들고 왔음, 전에는 이미 상용화된 서비스에도 사용했던 ModelMapperConfig만 외부 jar로 뺐지만
    //swagger도 그대로 사용하여 상용화에도 문제가 없다면 이것도 config로 빼는 것이 낫겠다.
    //또한 이 때는 의존 주입이 해당 jar에 설정되어 내 어플리케이션에서는 해당 dependency를 설정하지 않아도 되도록 하는 방법에 대해 탐구해보자
    /**
     * 현 API의 information이나 version, license
     * 정보등을 명시할 수 있다.
     *
     * 만들어진 Docket Bean의 Api정보로 추가해줘야 함
     * @return
     */
    @Order(value = 0)
    @Bean(name = "apiInfo")
    public ApiInfo getInfo() {
        /**
         * 라이센스, 버전, 연락처
         * TODO : 이런 부분들은 property로 외부에서 명시해주고 가져오는 것도 좋을듯
         */
        contact = new Contact("ishift", "http://www.ishift.co.kr/", "sjchoi@ishift.co.kr");
        info = new ApiInfoBuilder()
                .title("ISHIFT MSAMALL FRONT API")
                .license("ISC")
                .licenseUrl("https://opensource.org/licenses/ISC")
                .version("0.1.0")
                .contact(contact)
                .description("ISHIFT MSA MALL API FRONT PROJECT")
                .build();
        return info;
    }

    /**
     * springfox framework에서 제공해주는 docket빈 설정이 필요
     * swagger ui 사용시 (도메인)/(root)/swagger-ui.html로 ui에 접속할 수 있다.
     * @param apiInfo
     * @return
     */
    @Order(1)
    @Bean
    @DependsOn("apiInfo")
    public Docket swag(@Qualifier("apiInfo") ApiInfo apiInfo) {
        //Global예외처리 정의
		/* ResponseMessage dataError =
				new ResponseMessageBuilder().code(2222).message("Check Param!!!").build();
		ResponseMessage asyncError =
				new ResponseMessageBuilder().code(4444).message("Async Error!!!").build();
		ResponseMessage globalError =
				new ResponseMessageBuilder().code(9999).message("Error!!!").build();

		List<ResponseMessage> builderList = new ArrayList<>();

		builderList.add(dataError);
		builderList.add(asyncError);
		builderList.add(globalError); */

        return new Docket(DocumentationType.SWAGGER_2)	//인스턴스 생성 swagger2버전으로
                .select()							//builder
                //.apis(RequestHandlerSelectors.basePackage("com.ishift.apidemo.restcontroller"))	//swagger적용할 패키지 설정하고
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))	//swagger적용할 패키지 설정하고
                .paths(PathSelectors.any())			//그 안에 모든 path 전부를 사용
                .build()							//build
                .apiInfo(apiInfo)					//라이센스 명시
                .directModelSubstitute(LocalDate.class, String.class);		//LocalDate 클래스 파라미터에 대하여 String 지원되도록 명시
        //.globalResponseMessage(RequestMethod.GET, builderList)
        //.globalResponseMessage(RequestMethod.POST, builderList);	//글로벌 에러 처리
    }

    /**
     * https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/#plugin-system
     * swagger ui 설정
     */
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder
                .builder()
                //.deepLinking(true)
                //.displayOperationId(false)
                //.defaultModelsExpandDepth(1)
                //.defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(true)
                .docExpansion(DocExpansion.FULL)	// ???
                //.filter(false)					// ???
                //.maxDisplayedTags(null)			// ??
                .operationsSorter(null) // sort 없음
                .showExtensions(true)
                //.tagsSorter(TagsSorter.ALPHA)
                //.supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)	// ???
                //.validatorUrl(null)	// ???
                .build();
    }
}
