from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

@csrf_exempt
def start_game_session(request):
    # assert the method of the request( I.E get?)
    print(request.GET)
    
    return HttpResponse("start_game_session")

@csrf_exempt
def make_decision(request):
    return HttpResponse("make_decision")

@csrf_exempt
def end_session(request):
    return HttpResponse("end_game_session")