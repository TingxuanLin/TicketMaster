 (function() {
     var user_id = '1111';
     var user_fullname = 'John Smith';
     var lng = -122.08;
     var lat = 27.38;

     init(); // entry

     function init() {
         $('nearby-btn').addEventListener('click', loadNearbyItems);
         // $('fav-btn').addEventListener('click', loadFavoriteItems);
         // $('recommend-btn').addEventListener('click', loadRecommendedItems);

         var welcomeMsg = $('welcome-msg');
         welcomeMsg.innerHTML = 'Welcome, ' + user_fullname;

         initGeoLocation();
     }

     function initGeoLocation() {
         if (navigator.geolocation) {
             navigator.geolocation.getCurrentPosition(onPositionUpdated, onLoadPositionFailed, {
                 maximumAge:60000
             });
             showLoadingMessage("Retrieving your location...")
         } else {
             onLoadPositionFailed();
         }
     }

     function onPositionUpdated(position) {
         lat = position.coords.latitude;
         lng = position.coords.longitude;

         loadNearbyItems();
     }

     function onLoadPositionFailed() {
         console.warn("navigator geolocation is not available");
         getLocationFromIP();
     }

     function getLocationFromIP() {
         var url = "https://ipinfo.io/json";
         var req = null;
         ajax('GET', url, req, function(res) {
             var result = JSON.parse(res);
             if ('loc' in result) {
                 var loc = result.loc.split(',');
                 lat = loc[0];
                 lng = loc[1];
             } else {
                 console.warn("Getting location by IP failed.");
             }
             loadNearbyItems();
         });
     }

     function loadNearbyItems() {
         console.log('loadNearbyItems');
         activeBtn('nearby-btn');

         var url = './search';
         var params = 'user_id=' + user_id + '&lat=' + lat + '&lng=' + lng
         var req = JSON.stringify({});

         showLoadingMessage('Loading nearby items....');

         ajax('GET', url + '?' + params, req,
             function (res) {
                 var items = JSON.parse(res);
                 if (!items || items.length === 0) {
                     showWarningMessage('No nearby item.');
                 } else {
                     // listItems(items);
                 }
             },
             function() {
                showErrorMessage('Cannot load nearby items.');
             });
     }

     function activeBtn(btnId) {
         var btns = document.getElementsByClassName("main-nav-btn");

         // deactivate all navigation buttons
         for (var i = 0; i < btns.length; i++) {
             btns[i].className = btns[i].className
                 .replace(/\bactive\b/, '');
         }

         // active one that has id = btnId
         var btn = $(btnId);
         btn.className += 'active';
     }

     function showLoadingMessage(msg) {
         var itemList = $("item-list");
         itemList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i>'
                        + msg + '</p>';
     }

     function showWarningMessage(msg) {
         var itemList = $("item-list");
         itemList.innerHTML='<p class="notice"><i class="fa fa-exclamation-triangle"></i>'
                        + msg + '</p>';
     }

     function showErrorMessage(msg) {
         var itemList = $("item-list");
         itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-circle"></i>'
                        + msg + '</p>';
     }

     function listItems(items) {
         var itemList = $('item-list');
         itemList.innerHTML = '';

         for (var i = 0; i < items.length; i++) {
             addItem(itemList, items[i]);
         }
     }

     function addItem(itemList, item) {
         var item_id = item.item_id;

         var li = $('li', {
             id: 'item-' + item_id,
             className: 'item'
         });

         li.dataset.item_id = item_id;
         li.dataset.favorite = item.favorite;

         if (item.image_url) {
             li.appendChild($('img', {
                 src : item.image_url
             }));
         } else {
             li.appendChild($('img', {
                 src : 'https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png'
             }));
         }

         var section = $('div', {});

         var title = $('a', {
             href: item.url,
             target: '_blank',
             className: 'item-name'
         });
         title.innerHTML = item.name;
         section.appendChild(title);

         var category = $('p', {
             className: 'item-category'
         });
         category.innerHTML = 'Category: ' + item.categories.join()
            + section.appendChild(category);

         var stars = $('div', {
             className: 'stars'
         });

         for (var i = 0; i < item.rating; i++) {
             var star = $('i', {
                 className: 'fa fa-star'
             });
             stars.appendChild(star);
         }

         if (('' + item.rating).match(/\.5$/)) {
             stars.appendChild($('i', {
                 className: 'fa fa-star-half-o'
             }));
         }

         section.appendChild(stars);

         li.appendChild(section);

         var address = $('p', {
             className: 'item-address'
         });

         address.innerHTML = item.address.replace(/, /g, '<br/>')
             .replace(/\"/g,'');
         li.appendChild(address);

         var favLink = $('p', {
             className: 'fav-link'
         });

         favLink.onclick = function () {
             changeFavoriteItem(item_id);
         };

         favLink.appendChild($(i, {
             id: 'fav-icon-' + item_id,
             className: item.favorite ? 'fa fa-heart' : 'fa fa-heart-o'
         }));

         li.appendChild(favLink);

         itemList.appendChild(li);
     }
     /**
      * A helper function that creates a DOM element <tag options>
      * @param tag
      * @param options
      * @returns {HTMLElement|HTMLAnchorElement|HTMLAreaElement|HTMLAudioElement|HTMLBaseElement|HTMLQuoteElement|HTMLBodyElement|HTMLBRElement|HTMLButtonElement|HTMLCanvasElement|HTMLTableCaptionElement|HTMLTableColElement|HTMLDataElement|HTMLDataListElement|HTMLModElement|HTMLDetailsElement|HTMLDialogElement|HTMLDivElement|HTMLDListElement|HTMLEmbedElement|HTMLFieldSetElement|HTMLFormElement|HTMLHeadingElement|HTMLHeadElement|HTMLHRElement|HTMLHtmlElement|HTMLIFrameElement|HTMLImageElement|HTMLInputElement|HTMLLabelElement|HTMLLegendElement|HTMLLIElement|HTMLLinkElement|HTMLMapElement|HTMLMenuElement|HTMLMetaElement|HTMLMeterElement|HTMLObjectElement|HTMLOListElement|HTMLOptGroupElement|HTMLOptionElement|HTMLOutputElement|HTMLParagraphElement|HTMLPictureElement|HTMLPreElement|HTMLProgressElement|HTMLScriptElement|HTMLSelectElement|HTMLSlotElement|HTMLSourceElement|HTMLSpanElement|HTMLStyleElement|HTMLTableElement|HTMLTableSectionElement|HTMLTableCellElement|HTMLTemplateElement|HTMLTextAreaElement|HTMLTimeElement|HTMLTitleElement|HTMLTableRowElement|HTMLTrackElement|HTMLUListElement|HTMLVideoElement}
      */
     function $(tag, options) {
         if (!options) {
             return document.getElementById(tag);
             console.log("here");
         }
         var element = document.createElement(tag);

         for (var option in options) {
             if (options.hasOwnProperty(option)) {
                 element[option] = options[option];
             }
         }
         return element;
     }

     function ajax(method, url, data, callback, errorHandle) {
         var xhr = new XMLHttpRequest();

         xhr.open(method, url, true);

         xhr.onload = function () {
             if (xhr.status === 200) {
                 callback(xhr.responseText);
             } else if (xhr.status === 403) {
                 onSessionInvalid();
             } else {
                 errorHandle();
             }
         };

         xhr.onerror = function () {
             console.error("The request couldn't be completed.");
             errorHandle();
         };

         if (data === null) {
             xhr.send();
         } else {
             xhr.setRequestHeader("Content-Type",
                 "application/json;charset=utf-8");
             xhr.send(data);
         }
     }
 })()