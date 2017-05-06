package com.devternity.slidy;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.StandardSystemProperty.USER_HOME;

public class SlideHtmlTemplate {

    private final Url url;

    public SlideHtmlTemplate(Url url) {
        this.url = url;
    }

    public Slide slide(Deck.Dimensions dimensions) throws IOException, InterruptedException {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        CreateContainerResponse container = dockerClient
                .createContainerCmd("astefanutti/decktape")
                .withBinds(new Bind(destination().getParent(), new Volume("/slides")))
                .withCmd(url.toString(), destination().getName(), "--size", dimensions.x())
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        SysOut sysOutCallback = new SysOut();
        dockerClient.logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true)
                .withTailAll()
                .exec(sysOutCallback);

        sysOutCallback.awaitCompletion(10, TimeUnit.SECONDS);

        return new Slide(destination());
    }

    private File destination() throws IOException {
        File tempDir = new File(USER_HOME.value(), "slidy");
        tempDir.mkdir();
        return File.createTempFile("deck", ".slide", tempDir);
    }

    class SysOut extends ResultCallbackTemplate<LogContainerResultCallback, Frame> {

        @Override
        public void onNext(Frame object) {
            System.out.println(object);
        }
    }

}
