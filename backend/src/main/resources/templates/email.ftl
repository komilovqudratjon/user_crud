<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body style="margin: 0; padding: 0;">
<div style="width: auto;
            padding: 1px;
            border: 3px solid blue;
            text-align: center;
            background-color: #F5F7F8;
            border-radius: 10px;
            font-family:'JetBrains Mono Medium' ,Arial, sans-serif; font-size: 16px;">
    <div style="padding: 1px 0;">
        <div style="width:100%; flex-direction: column!important; padding: 2px 0; margin: 0; background-color: white; border: 0; border-radius: 10px;">
            <h3>    ${name}</h3>
            <p style="color:green;">Вам нужно пройти тест. Ваш логин и пароль:</p>
            <div>
                <p><i style="color:red;">Логин:</i> ${user}</p>
                <p><i style="color:red;">Парол:</i> ${pass}</p>
            </div>
            <p>Линк для входа: <a style="text-decoration: none" href="http://${ip}">${ip}</a></p>
        </div>
        <div style="width:100%; display: inline-block; flex-direction: row; padding: 0px; margin: 0;">
            <div style="width:100%; display: grid; padding: 0px; margin: 10px 30px; color: black; font-size: 14px;">
                <p style="float: left; color: black; font-weight: bold; display: block; margin: 3px 0; text-align: left;">Адрес: 100170,
                    г.Ташкент, Мирзо-Улугбекский район, проспект
                    Мустакиллик, 107.</p>
                <p style="float: left; margin: 3px 0;display: block; text-align: left;"> Тел: (71) 203-01-15, (71) 203-01-14</p>
            </div>
            <div style="width:100%; display: inline-block; padding: 0px; margin: 10px 30px; color: black; font-size: 14px;">
                <p style="text-align: left;">Мы в соц. сетях </p>
                <div style="display: flex; float: left;">
                    <a style="padding: 10px 10px 7px; margin-right: 10px;  color: #ffff;"
                       href="https://www.facebook.com/pg/cmdagency.uz/posts/">
                        <img width="30px" src="https://cdn.freebiesupply.com/images/large/2x/facebook-logo-circle.png" alt="facebook">
                    </a>
                    <a style="padding: 10px 10px 7px; margin-right: 10px; color: #ffff;"
                       href="https://www.youtube.com/channel/UCQIKib6Z1CNUYZunFJ1sCaA/featured">
                        <img width="30px" src="https://www.clipartmax.com/png/full/95-957976_transparent-youtube-logo-clipart-portrait-of-a-man.png" alt="youtube">
                    </a>
                    <a style="padding: 10px 10px 7px; margin-right: 10px;  color: #ffff;"
                       href="https://www.instagram.com/cmda.uz/" >
                        <img width="33px" src="https://nakruti-like.ru/wp-content/uploads/2020/02/instagramm-clipart-svg-5.png" alt="instagram">
                    </a>
                    <a style="padding:10px 10px 7px; margin-right: 10px;  color: #ffff;"
                       href="https://t.me/CMDAuz">
                        <img width="32px" src="https://www.citadel-engineering.com/wp-content/uploads/telegram-blue-icon.png" alt="telegram">
                    </a>
                </div>

            </div>
        </div>
    </div>
</div>
</body>
</html>