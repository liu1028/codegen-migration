package ${basePackage}.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.to8to.common.search.PageResult;
import com.to8to.sc.compatible.RPCException;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;

import javax.validation.Valid;

@RestController
@RequestMapping("${baseRequestMapping}")
@Api(description = "${apiDesciption}")
public class ${className} {

@Autowired
private ${serviceClz} ${serviceVar};

//循环

}