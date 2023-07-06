# 使用超小的Linux镜像alpine
FROM alpine:3.12

ENV HOME /root

# 安装准备
RUN    apk update \
        && apk add --no-cache --virtual .build-deps bash gcc libc-dev make openssl-dev pcre-dev zlib-dev linux-headers curl gnupg libxslt-dev gd-dev geoip-dev git wget

# 复制工具
ADD soft ${HOME}

RUN cd ${HOME} \
    && tar xvf libfastcommon-master.tar.gz \
    && tar xvf fastdfs-master.tar.gz \
    && tar xvf fastdfs-nginx-module-master.tar.gz \
    && tar xvf fastdht-master.tar.gz \
    && tar xvf nginx-1.21.2.tar.gz \
    && tar xvf db-18.1.32.tar.gz

# 下载libfastcommon、fastdfs、fastdfs-nginx-module、fastdht、berkeley-db、nginx插件的源码
#RUN     cd ${HOME} \
#        && curl -L https://github.com/happyfish100/libfastcommon/archive/master.tar.gz -o libfastcommon-master.tar.gz \
#        && curl -L https://github.com/happyfish100/fastdfs/archive/master.tar.gz -o fastdfs-master.tar.gz \
#        && curl -L https://github.com/happyfish100/fastdfs-nginx-module/archive/master.tar.gz -o fastdfs-nginx-module-master.tar.gz \
#        && curl -L https://github.com/happyfish100/fastdht/archive/master.tar.gz -o fastdht-master.tar.gz \
#        && curl -L http://nginx.org/download/nginx-1.21.2.tar.gz -o nginx-1.21.2.tar.gz \
#        && wget --http-user=username --http-passwd=password https://edelivery.oracle.com/akam/otn/berkeley-db/db-18.1.32.tar.gz \
#        && tar xvf libfastcommon-master.tar.gz \
#        && tar xvf fastdfs-master.tar.gz \
#        && tar xvf fastdfs-nginx-module-master.tar.gz \
#        && tar xvf fastdht-master.tar.gz \
#        && tar xvf nginx-1.21.2.tar.gz \
#        && tar xvf db-18.1.32.tar.gz

# 安装libfastcommon
RUN     cd ${HOME}/libfastcommon-master/ \
        && ./make.sh \
        && ./make.sh install

# 安装fastdfs
RUN     cd ${HOME}/fastdfs-master/ \
        && ./make.sh \
        && ./make.sh install

#安装berkeley db
WORKDIR ${HOME}/db-18.1.32/build_unix
RUN     ../dist/configure -prefix=/usr \
        && make \
        && make install

#安装fastdht
RUN     cd ${HOME}/fastdht-master/ \
        && sed -i "s?CFLAGS='-Wall -D_FILE_OFFSET_BITS=64 -D_GNU_SOURCE'?CFLAGS='-Wall -D_FILE_OFFSET_BITS=64 -D_GNU_SOURCE -I/usr/include/ -L/usr/lib/'?" make.sh \
        && ./make.sh \
        && ./make.sh install

# 配置fastdfs: base_dir
RUN     cd /etc/fdfs/ \
        && cp storage.conf.sample storage.conf \
        && cp tracker.conf.sample tracker.conf \
        && cp client.conf.sample client.conf \
        && sed -i "s|/home/yuqing/fastdfs|/var/local/fdfs/tracker|g" /etc/fdfs/tracker.conf \
        && sed -i "s|/home/yuqing/fastdfs|/var/local/fdfs/storage|g" /etc/fdfs/storage.conf \
        && sed -i "s|/home/yuqing/fastdfs|/var/local/fdfs/storage|g" /etc/fdfs/client.conf \
        && sed -i "s|/home/yuqing/fastdht|/var/local/fdht|g" /etc/fdht/fdhtd.conf \
        && sed -i "s|/home/yuqing/fastdht|/var/local/fdht|g" /etc/fdht/fdht_client.conf

# 获取nginx源码，与fastdfs插件一起编译
RUN     cd ${HOME} \
        && chmod u+x ${HOME}/fastdfs-nginx-module-master/src/config \
        && cd nginx-1.21.2 \
        && ./configure --add-module=${HOME}/fastdfs-nginx-module-master/src \
        && make && make install

# 设置nginx和fastdfs联合环境，并配置nginx
RUN     cp ${HOME}/fastdfs-nginx-module-master/src/mod_fastdfs.conf /etc/fdfs/ \
        && sed -i "s|^store_path0.*$|store_path0=/var/local/fdfs/storage|g" /etc/fdfs/mod_fastdfs.conf \
        && sed -i "s|^url_have_group_name =.*$|url_have_group_name = true|g" /etc/fdfs/mod_fastdfs.conf \
        && cd ${HOME}/fastdfs-master/conf/ \
        && cp http.conf mime.types anti-steal.jpg /etc/fdfs/ \
        && echo -e "\
           events {\n\
           worker_connections  1024;\n\
           }\n\
           http {\n\
           server_tokens off;\n\
           include       mime.types;\n\
           default_type  application/octet-stream;\n\
           server {\n\
               listen 80;\n\
               server_name localhost;\n\
               location ~ /group[0-9]/M00 {\n\
                 ngx_fastdfs_module;\n\
               }\n\
             }\n\
            }" >/usr/local/nginx/conf/nginx.conf

# 清理文件
RUN rm -rf ${HOME}/*
RUN apk del .build-deps gcc libc-dev make openssl-dev linux-headers curl gnupg libxslt-dev gd-dev geoip-dev
RUN apk add bash pcre-dev zlib-dev

# 设置时区
ENV TZ Asia/Shanghai
RUN apk add -U tzdata
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 防盗链参数配置
# 是否做token检查，缺省值为false
ENV CHECK_TOKEN false
# 生成token的有效时长，默认900s
ENV TOKEN_TTL 900
# 生成token的密钥
ENV SECRET_KEY FastDFS1234567890
# token检查失败，返回的本地文件内容
ENV TOKEN_CHECK_FAIL /etc/fdfs/anti-steal.jpg

# 配置启动脚本，在启动时中根据环境变量替换nginx端口、fastdfs端口
# 默认nginx端口
ENV WEB_PORT 80
# 默认track_server端口
ENV FDFS_PORT 22122
# 默认storage_server端口
ENV STORAGE_PORT 23000
# 默认fastdht端口
ENV FDHT_PORT 11411
# 创建启动脚本
RUN echo -e "\
sed -i \"s/http.anti_steal.check_token=.*$/http.anti_steal.check_token=\$CHECK_TOKEN/g\" /etc/fdfs/http.conf; \n\
sed -i \"s/http.anti_steal.token_ttl=.*$/http.anti_steal.token_ttl=\$TOKEN_TTL/g\" /etc/fdfs/http.conf; \n\
sed -i \"s/http.anti_steal.secret_key=.*$/http.anti_steal.secret_key=\$SECRET_KEY/g\" /etc/fdfs/http.conf; \n\
sed -i \"s|/home/yuqing/fastdfs/conf/anti-steal.jpg|\$TOKEN_CHECK_FAIL|g\" /etc/fdfs/http.conf; \n\
mkdir -p /var/local/fdfs/storage/data /var/local/fdfs/tracker /var/local/fdht; \n\
sed -i \"s/listen\ .*$/listen\ \$WEB_PORT;/g\" /usr/local/nginx/conf/nginx.conf; \n\
sed -i \"s/http.server_port=.*$/http.server_port=\$WEB_PORT/g\" /etc/fdfs/storage.conf; \n\
if [ \"\$IP\" = \"\" ]; then \n\
    IP=\`ifconfig eth0 | grep inet | awk '{print \$2}'| awk -F: '{print \$2}'\`; \n\
fi \n\
IP=(\${IP//,/ }); \n\
sed -i '/^group/,\$d' /etc/fdht/fdht_servers.conf; \n\
sed -i '\$a\group_count = '\${#IP[*]}'' /etc/fdht/fdht_servers.conf; \n\
for ((i=0; i<\${#IP[*]}; i++)) \n\
  do \n\
    sed -i '\$a\group'\${i}'='\${IP[i]}':'\$FDHT_PORT'' /etc/fdht/fdht_servers.conf; \n\
  done \n\
sed -i \"s/^tracker_server=.*$/tracker_server=\${IP[0]}:\$FDFS_PORT/g\" /etc/fdfs/client.conf; \n\
sed -i \"s/^tracker_server=.*$/tracker_server=\${IP[0]}:\$FDFS_PORT/g\" /etc/fdfs/storage.conf; \n\
sed -i \"s/^tracker_server=.*$/tracker_server=\${IP[0]}:\$FDFS_PORT/g\" /etc/fdfs/mod_fastdfs.conf; \n\
for ((i=1; i<\${#IP[*]}; i++)) \n\
  do \n\
    sed -i \"/tracker_server=\${IP[i-1]}:\$FDFS_PORT/atracker_server=\${IP[i]}:\$FDFS_PORT\" /etc/fdfs/client.conf; \n\
    sed -i \"/tracker_server=\${IP[i-1]}:\$FDFS_PORT/atracker_server=\${IP[i]}:\$FDFS_PORT\" /etc/fdfs/storage.conf; \n\
    sed -i \"/tracker_server=\${IP[i-1]}:\$FDFS_PORT/atracker_server=\${IP[i]}:\$FDFS_PORT\" /etc/fdfs/mod_fastdfs.conf; \n\
  done \n\
sed -i \"s/^port=.*$/port=\$FDFS_PORT/\" /etc/fdfs/tracker.conf; \n\
sed -i \"s/^port=.*$/port=\$STORAGE_PORT/g\" /etc/fdfs/storage.conf; \n\
sed -i \"s/^storage_server_port=.*$/storage_server_port=\$STORAGE_PORT/g\" /etc/fdfs/mod_fastdfs.conf; \n\
sed -i \"s/^port=.*$/port=\$FDHT_PORT/g\" /etc/fdht/fdhtd.conf; \n\
sed -i \"s/^check_file_duplicate=.*$/check_file_duplicate=1/g\" /etc/fdfs/storage.conf; \n\
sed -i \"s/^keep_alive=.*$/keep_alive=1/g\" /etc/fdfs/storage.conf; \n\
sed -i \"s/^##include \/home\/yuqing\/fastdht\/conf\/fdht_servers.conf/#include \/etc\/fdht\/fdht_servers.conf/g\" /etc/fdfs/storage.conf; \n\
/etc/init.d/fdfs_trackerd start; \n\
/usr/local/bin/fdhtd /etc/fdht/fdhtd.conf; \n\
/etc/init.d/fdfs_storaged start; \n\
/usr/local/nginx/sbin/nginx; \n\
tail -f /usr/local/nginx/logs/access.log \
">/start.sh \
&& chmod u+x /start.sh
ENTRYPOINT ["/bin/bash","/start.sh"]
