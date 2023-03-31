import {getRequest} from "./api";

export const initMenu = (router,store)=>{
    if (store.state.routes.length>0){
        return;
    }

    getRequest('/system/cfg/menu').then(data=>{
        if (data){
            //格式化Router
            let fmtRoutes = formatRoutes(data);
            //添加到router
            // router.addRoutes(fmtRoutes);//新版已经弃用
            for (let r of fmtRoutes){
                router.addRoute(r);
            }
            //将数据存入vuex
            store.commit('initRoutes',fmtRoutes);
        }
    })
}

export const formatRoutes = (routes)=>{
    let fmtRoutes = [];
    routes.forEach(router=>{
        let {
            path,
            component,
            name,
            iconCls,
            children,
        } = router;
        if (children && children instanceof Array){
            //递归
            children = formatRoutes(children);
        }
        let fmRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            children: children,
            component(resolve) {
                if (component.startsWith('Home')){
                    require(['../views/'+ component + '.vue'], resolve);
                }else{
                    require(['../views/'+component.substr(0,3).toLowerCase()+'/' + component + '.vue'], resolve);
                }
            }
        }
        fmtRoutes.push(fmRouter)
    });
    return fmtRoutes;
}