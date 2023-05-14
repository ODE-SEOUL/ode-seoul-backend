package kr.njw.odeseoul.common.config;

import io.imagekit.sdk.ImageKit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageKitConfig {
    @Value("${ode-seoul.image-kit.url-endpoint}")
    private String urlEndpoint;
    @Value("${ode-seoul.image-kit.public-key}")
    private String publicKey;
    @Value("${ode-seoul.image-kit.private-key}")
    private String privateKey;

    @Bean
    public ImageKit imageKit() {
        ImageKit imageKit = ImageKit.getInstance();
        io.imagekit.sdk.config.Configuration config = new io.imagekit.sdk.config.Configuration(this.publicKey, this.privateKey, this.urlEndpoint);
        imageKit.setConfig(config);
        return imageKit;
    }
}
