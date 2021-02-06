// ==UserScript==
// @name         豆瓣视频下载
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       oleolema
// @match        https://movie.douban.com/subject/**
// @grant        none
// ==/UserScript==

(function() {
    'use strict';
    if(!location.href.startsWith('https://movie.douban.com/subject/')){
        return ;
    }
    const searchHost = "http://localhost:8060"
    const movieName = document.title;
    const indent = $('.indent.clearfix');
    indent.append("<br>")
    console.info(movieName);
    console.info(indent);
    const simpleName = movieName.split(/\s+/)[0];
    searchMovie(simpleName, 'baidu')
    searchMovie(simpleName, 'ok')

    function searchMovie(movieName, type){
        const id = `movie_resource_${type}`
        indent.append(`<div id="${id}" style="text-align: center;"><b style="color:green;">Loading...</b></div>`);
        const movieResources = indent.get(0).querySelector(`#${id}`);
        $.ajax({
            url: `${searchHost}/movie/search?s=${movieName}&type=${type}`,
            type: 'GET',
            success: function(res){
                if(res.status !== 200){
                    movieResources.innerHTML = `<span style="color:red">${res.message}</span>`;
                    return;
                }
                movieResources.innerHTML = (res.data.map(it=>`<div style="color:black;border: 1px solid black;margin: 10px 0;padding: 0 10px;"><p><b>${it.title}</b></p>${it.resources.map(j=>j.replace(/https?:\/\/[-A-Za-z0-9+&@#\/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]/g, ($0)=>`<a rel="alternate" download target="__black" href=${$0}>${$0}</a>`)).join("<br>")}</div>`).join(" "))

            },
            error: function(res) {
                if(res.message){
                    movieResources.innerHTML = `<span style="color:red">${res.message}</span>`;
                    return;
                }
                movieResources.innerHTML = `<span style="color:red">${type}资源中未找到该电影</span>`;
            }
        });
    }



    // Your code here...
})();