FROM nginx:1.23.2

WORKDIR /etc/nginx

RUN mkdir /etc/nginx/sites-enabled

RUN mkdir /etc/nginx/sites-available

RUN cp conf.d/default.conf /etc/nginx/sites-available

RUN ln -s /etc/nginx/sites-available/default.conf /etc/nginx/sites-enabled/
