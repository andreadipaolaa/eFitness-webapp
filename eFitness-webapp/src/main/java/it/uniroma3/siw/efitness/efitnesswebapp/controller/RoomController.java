package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Room;
import it.uniroma3.siw.efitness.efitnesswebapp.service.RoomService;
import it.uniroma3.siw.efitness.efitnesswebapp.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("admin/room/")
public class RoomController {

    @Autowired
    private RoomService roomService;

    public static String DIR = System.getProperty("user.dir")+"/eFitness-webapp/src/main/resources/static/images/room/";

    @RequestMapping(value={"list"}, method = RequestMethod.GET)
    public String getRooms(Model model){
        model.addAttribute("rooms", this.roomService.getAll());
        return "admin/room-list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addRoom (Model model) {
        model.addAttribute("room", new Room());
        return "admin/room-form";
    }


    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addRoom(@ModelAttribute("room") Room room, @RequestParam("image") MultipartFile multipartFile, Model model) {
        room.setPhoto(savePhoto(multipartFile,room));
        roomService.save(room);
        return getRooms(model);
    }


    @RequestMapping(value={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyRoom(@PathVariable("id") Long idRoom, Model model){
        model.addAttribute("room", this.roomService.getRoomById(idRoom));
        return "admin/room-modify-form";
    }

    @RequestMapping(value = {"modify/{id}"}, method = RequestMethod.POST)
    public String modifyRoom(@PathVariable("id") Long idRoom, @ModelAttribute("course") Room room,
                               @RequestParam("image")MultipartFile multipartFile, Model model){
        room.setPhoto(modifyPhoto(multipartFile, idRoom, room));
        this.roomService.modifyById(idRoom,room);
        return "admin/room-list";
    }


    public String savePhoto(MultipartFile multipartFile, Room room){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = getUploadDir(room);
        FileManager.store(multipartFile, uploadDir);
        return fileName;
    }

    public String modifyPhoto(MultipartFile multipartFile, Long id, Room newRoom){
        Room oldRoom = this.roomService.getRoomById(id);
        if(! multipartFile.isEmpty()){
            FileManager.removeImgAndDir(getUploadDir(oldRoom), oldRoom.getPhoto());
            return savePhoto(multipartFile, newRoom);
        }
        else{
            FileManager.dirChangeName(getUploadDir(oldRoom), getUploadDir(newRoom));
            return oldRoom.getPhoto();
        }
    }

    public String getUploadDir(Room room){
        return DIR + room.getName().replaceAll("\\s", "");
    }
}
