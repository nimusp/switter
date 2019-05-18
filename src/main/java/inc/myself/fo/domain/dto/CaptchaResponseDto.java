package inc.myself.fo.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {

    @JsonAlias("success")
    private boolean isSuccess;

    @JsonAlias("hostname")
    private String hostName;

    @JsonAlias("error-codes")
    private List<String> errorCodeSet;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public List<String> getErrorCodeSet() {
        return errorCodeSet;
    }

    public void setErrorCodeSet(List<String> errorCodeSet) {
        this.errorCodeSet = errorCodeSet;
    }
}
