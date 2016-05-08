var dataLimit =10;
var pageNum =0;
var filter ="filter=all";
var searchData= function(id){
	var callBack ={
			success : reloadGrid,
			failure : failure
	}
	var searchBoxVal = document.getElementById("searchBox").value;
	var params ="limit=10&pageNum=0";
	if(searchBoxVal != "" && searchBoxVal != null)
		params+= "&search="+searchBoxVal;
	params+="&"+filter;
	AjaxConnect.sendAsynchRequest('GET','../SendRequest.Action',callBack,params);
}


var reloadGrid = function(data){
	var container = document.getElementById('dataWindow');	
	var responseData = eval(data.responseText);
	$('#dataWindow').empty();

	if(responseData.length >0){
		for(var eachData in responseData){
			var aDiv = document.createElement('div');
			aDiv.className = 'eachResult floatLeft wid';

			var eachDiv = document.createElement('div');
			eachDiv.className = 'floatLeft threeFourthWidth';
			eachDiv.innerHTML = "<img height='100px' width='200px'  src='ad.png' class='floatLeft'></img>"+
			"<h3 style='padding-left:215px'>"+responseData[eachData].name+'</h3>';

			eachDiv.appendChild(createRatingSpan(responseData[eachData].rating));
			aDiv.appendChild(eachDiv);

			aDiv.innerHTML += "<div class='floatRight oneFourthWidth'  >"+
			"<h4 > "+"<span class='glyphicon glyphicon-pushpin' style='color:#119DC9'></span>"+responseData[eachData].city+"<span>,</span>"+responseData[eachData].country+"</h4>"+
			"<h4 > "+"<span class='glyphicon glyphicon-send'></span>"+" Elevation:"+gracefulData(responseData[eachData].elev)+"<span>ft.</span>"+"</h4>"+
			"<h4 > "+"<span class='glyphicon glyphicon-plane ' style='color:orange'></span>"+" Direct Flight:"+responseData[eachData].direct+"</h4>"+
			" </div>";
			container.appendChild(aDiv);
		}
	}else{
		var aDiv = document.createElement('div');
		aDiv.className = 'eachResult floatLeft wid';

		aDiv.innerHTML= "<img height='200px' width='200px'  src='minion.png' class='floatLeft'></img>"+
		"<h2 class='floatLeft paddingLeft'> OOPS!! Nothing matches..Minion says to search different.</h2>";
		container.appendChild(aDiv);
	}

	//alert(b[eachData].url);

}
var createRatingSpan= function(rating){
	var aDiv = document.createElement('div');
	aDiv.style.paddingLeft = '215px';
	if(rating != "" && rating != null ){
		var stars=Math.round(rating);
		for(var i=0; i<stars; i++){
			var spanDiv = document.createElement('span');
			spanDiv.className ='glyphicon glyphicon-star';
			aDiv.appendChild(spanDiv);
		}
		for(var i=stars; i<5; i++){
			var spanDiv = document.createElement('span');
			spanDiv.className ='glyphicon glyphicon-star-empty';
			aDiv.appendChild(spanDiv);
		}


	}else{
		for(var i=0; i<5; i++){
			var spanDiv = document.createElement('span');
			spanDiv.className ='glyphicon glyphicon-star-empty';
			aDiv.appendChild(spanDiv);
		}

	}	
	return aDiv;	
}

var gracefulData = function(data){
	if(data == ""){
		return 0;
	}
	return data;
}
var failure = function(){
	alert("failure");
}

$(function(){
	console.log('ready');

	$('.list-group a').click(function(e) {
		e.preventDefault()

		$that = $(this);

		$that.parent().find('a').removeClass('active');
		$that.addClass('active');
		filter = "filter="+$that.attr('id');
		searchData();
	});
})

var resetDefaults = function(){
	document.getElementById('searchBox').value='';
}
/*$(function () {
    $('[id*=searchBox]').typeahead({
        hint: true,
        highlight: true,
        minLength: 1
        ,source: function (request, response) {
            $.ajax({
                url: '../SendRequest.Action',
                data: "{ 'prefix': '" + request + "'}",
                dataType: "json",
                type: "GET",
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    items =  eval(data.responseText);

                    map = {};
                    for(var eachData in responseData){

                    $.each(data.d, function (i, item) {
                        var id = item.split('-')[1];
                        var name = item.split('-')[0];
                        map[name] = { id: id, name: name };
                        items.push(name);
                    });
                    response(items);
                    $(".dropdown-menu").css("height", "auto");
                },
                error: function (response) {
                    alert(response.responseText);
                },
                failure: function (response) {
                    alert(response.responseText);
                }
            });
        },
        updater: function (item) {
            $('[id*=hfCustomerId]').val(map[item].id);
            return item;
        }
    });
});*/