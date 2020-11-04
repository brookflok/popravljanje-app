import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IOdgovorNaJavnoPitanje } from 'app/shared/model/odgovor-na-javno-pitanje.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './odgovor-na-javno-pitanje.reducer';

export interface IOdgovorNaJavnoPitanjeDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OdgovorNaJavnoPitanjeDeleteDialog = (props: IOdgovorNaJavnoPitanjeDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/odgovor-na-javno-pitanje');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.odgovorNaJavnoPitanjeEntity.id);
  };

  const { odgovorNaJavnoPitanjeEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="popravljanjeApp.odgovorNaJavnoPitanje.delete.question">
        <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.delete.question" interpolate={{ id: odgovorNaJavnoPitanjeEntity.id }}>
          Are you sure you want to delete this OdgovorNaJavnoPitanje?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-odgovorNaJavnoPitanje" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ odgovorNaJavnoPitanje }: IRootState) => ({
  odgovorNaJavnoPitanjeEntity: odgovorNaJavnoPitanje.entity,
  updateSuccess: odgovorNaJavnoPitanje.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OdgovorNaJavnoPitanjeDeleteDialog);
