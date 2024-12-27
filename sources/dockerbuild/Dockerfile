FROM ubuntu:16.04
ARG DEBIAN_FRONTEND=noninteractive
ARG USER_DEFAULT=dev
ENV DEVUSER=${USER_DEFAULT}
RUN apt-get -y update && apt-get -y upgrade \
    && apt-get install -y wget unzip make g++ python git diffstat texinfo \
    gawk chrpath libsdl1.2-dev build-essential socat xterm xsltproc \
    docbook-utils dblatex xmlto xutils-dev gcc-multilib git-core nfs-common \
    patchutils screen openssh-server ccache locales cpio sudo net-tools \
    libelf-dev python3-django vim
RUN groupadd -r admin
RUN useradd -m -s /bin/bash -U ${DEVUSER} -G admin
RUN echo "${DEVUSER}:${DEVUSER}" | chpasswd
RUN mkdir -p /home/${DEVUSER}/builddirs /home/${DEVUSER}/project
RUN chown ${DEVUSER}:${DEVUSER} /home/${DEVUSER}/builddirs -R
RUN chown ${DEVUSER}:${DEVUSER} /home/${DEVUSER}/project -R
RUN su ${DEVUSER} -c "ssh-keygen -f /home/${DEVUSER}/.ssh/id_rsa -t rsa -N ''"
RUN su ${DEVUSER} -c "git config --global user.name ${DEVUSER}"
RUN su ${DEVUSER} -c "git config --global user.email ${DEVUSER}@localhost"
RUN su ${DEVUSER} -c "echo -e 'host github.com\n' \
            'user git\n' \
            'hostname ssh.github.com\n' \
            'identityfile ~/.ssh/id_rsa\n' \
            'port 443\n' > /home/${DEVUSER}/.ssh/config"
COPY dockerfiles/authorized_keys /home/${DEVUSER}/.ssh/authorized_keys
RUN chown ${DEVUSER}:${DEVUSER} /home/${DEVUSER}/.ssh/authorized_keys
COPY dockerfiles/start-yocto.sh /home/$DEVUSER/start-yocto.sh
COPY project.conf /project.conf
RUN ln -s /home/$DEVUSER/start-yocto.sh /home/$DEVUSER/.bash_aliases
COPY dockerfiles/init.rc /init.rc
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ENTRYPOINT /init.rc
