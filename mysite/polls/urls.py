from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^lighton$', views.lighton, name='lighton'),
    url(r'^sleep$', views.sleep, name='sleep'),
    url(r'^address$', views.address, name='address'),
    url(r'^environment$', views.environment, name='environment'),
]
